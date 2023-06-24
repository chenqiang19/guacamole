package com.ict.service.Impl;
;
import com.ict.constants.GuacamoleEntityType;
import com.ict.db.entity.*;
import com.ict.db.service.*;
import com.ict.domain.GuacamoleInfo;
import com.ict.domain.GuacamoleParameter;
import com.ict.service.GuacamoleService;
import com.ict.utils.ObjectTools;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GuacamoleServiceImpl implements GuacamoleService {

    @Resource
    private GuacamoleConnectGroupService guacamoleConnectGroupService;

    @Resource
    private GuacamoleConnectService guacamoleConnectService;

    @Resource
    private GuacamoleUserService guacamoleUserService;

    @Resource
    private GuacamoleEntityService guacamoleEntityService;

    @Resource
    private GuacamoleConnectParameterService guacamoleConnectParameterService;

    @Value(value = "${guacd.ip}")
    private String hostname;

    @Value(value = "${guacd.port}")
    private Long port;

    @Override
    public void insertGuacamoleInfo(GuacamoleInfo guacamoleInfo) throws NoSuchAlgorithmException {
        /*
        * 1、 查询Group是否存在，存在则不插入数据库，不存在这插入(guacamole中的Group，对应openstack的项目)
        * 2、 查询User是否存在，如果不存在，则插入数据库(guacamole的User对应openstack中的用户)
        * 3、 查询实例是否在guacamole数据库中，不在则插入
        * */

        if (guacamoleInfo == null) {
            throw new IllegalArgumentException("guacamole info is null");
        }

        // 1 获取组并判断是否插入
        List<GuacamoleConnectGroupEntity> groups = guacamoleConnectGroupService.list();
        List<String> groupNames = groups.stream().map(GuacamoleConnectGroupEntity::getConnectionGroupName).collect(Collectors.toList());
        if (!groupNames.contains(guacamoleInfo.getProjectName())) {
            GuacamoleConnectGroupEntity guacamoleConnectGroupEntity = new GuacamoleConnectGroupEntity();
            // 项目ID必须唯一，所以使用组名存储项目ID，而非项目名称
            guacamoleConnectGroupEntity.setConnectionGroupName(guacamoleInfo.getProjectName());
            guacamoleConnectGroupService.save(guacamoleConnectGroupEntity);
        }

        // 2 获取用户并判断是否插入
        List<GuacamoleUserEntity> users = guacamoleUserService.list();
        List<String> userIds = users.stream().map(GuacamoleUserEntity::getFullName).collect(Collectors.toList());
        if (!userIds.contains(guacamoleInfo.getUserId())) {
            GuacamoleUserEntity guacamoleUserEntity = new GuacamoleUserEntity();
            // 用户ID必须唯一，openstack中user_id为string类型，所以用fullName存储userId
            guacamoleUserEntity.setFullName(guacamoleInfo.getUserId());
            try {
                final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(guacamoleInfo.getUserPassword().getBytes(StandardCharsets.UTF_8));
                guacamoleUserEntity.setPasswordHash(messageDigest.digest());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                throw new NoSuchAlgorithmException("SHA-256 is not found");
            }
            GuacamoleEntity guacamoleEntity = new GuacamoleEntity();
            guacamoleEntity.setName(guacamoleInfo.getUserId());
            guacamoleEntity.setType(GuacamoleEntityType.USER);
            guacamoleEntityService.save(guacamoleEntity);

            guacamoleUserEntity.setEntityId(guacamoleEntity.getEntityId());
            Timestamp time = new Timestamp(new Date().getTime());
            guacamoleUserEntity.setPasswordDate(time);
            guacamoleUserEntity.setDisabled(0);
            guacamoleUserEntity.setExpired(0);
            guacamoleUserService.save(guacamoleUserEntity);
        }

        // 3 获取连接，并判断是否插入
        List<GuacamoleConnectEntity> connects = guacamoleConnectService.list();
        List<String> connectNames = connects.stream().map(GuacamoleConnectEntity::getConnectionName).collect(Collectors.toList());
        GuacamoleConnectEntity guacamoleConnectEntity = null;
        if (!connectNames.contains(guacamoleInfo.getInstanceName())) {
            guacamoleConnectEntity = new GuacamoleConnectEntity();
            // 实例的ID必须唯一，所以这里使用实例的ID作为connectName
            guacamoleConnectEntity.setConnectionName(guacamoleInfo.getInstanceName());
            guacamoleConnectEntity.setProtocol(guacamoleInfo.getProtocol());
            guacamoleConnectEntity.setProxyHostname(hostname);
            guacamoleConnectEntity.setProxyPort(port);
            guacamoleConnectService.save(guacamoleConnectEntity);
        } else {
            return;
        }

        // 4 判断连接参数是否存在
        List<GuacamoleConnectParameterEntity> parameters = guacamoleConnectParameterService.list();
        List<Long> parameterIds = parameters.stream().map(GuacamoleConnectParameterEntity::getConnectionId).collect(Collectors.toList());
        if (!parameterIds.contains(guacamoleConnectEntity.getConnectionId())) {

            try {
                GuacamoleConnectParameterEntity guacamoleConnectParameterEntity = new GuacamoleConnectParameterEntity();
                guacamoleConnectParameterEntity.setConnectionId(guacamoleConnectEntity.getConnectionId());
                Map<String, Object> parameterMap = ObjectTools.objToMap(guacamoleInfo.getGuacamoleParameters());

                for (String key : parameterMap.keySet()) {
                    String value = (String) parameterMap.get(key);
                    if (value != null) {
                        if (key.equals("ignorecert")) {
                            key="ignore-cert";
                        }
                        guacamoleConnectParameterEntity.setParameterName(key);
                        guacamoleConnectParameterEntity.setParameterValue(value);
                        guacamoleConnectParameterService.save(guacamoleConnectParameterEntity);
                    }
                }
            } catch (NullPointerException e) {
                throw new IllegalArgumentException("guacamole parameters if null");
            }
        }
    }

    @Override
    public void modifiedParameter(GuacamoleParameter guacamoleParameter, String connectionId) {
        List<GuacamoleConnectEntity> guacamoleConnectEntities = guacamoleConnectService.selectByInstanceId(connectionId);
        if (guacamoleConnectEntities==null || guacamoleConnectEntities.size()==0) {
            throw new IllegalArgumentException(connectionId + ": guacamole connect is not exist");
        }
        List<GuacamoleConnectParameterEntity> guacamoleConnectParameterEntities = guacamoleConnectParameterService.selectByConnectId(guacamoleConnectEntities.get(0).getConnectionId());

        guacamoleConnectParameterEntities = guacamoleConnectParameterEntities.stream().filter(p ->
                p.getParameterName().equals(guacamoleParameter.getParameterName())).collect(Collectors.toList());

        if (guacamoleConnectParameterEntities==null || guacamoleConnectParameterEntities.size()==0) {
            throw new IllegalArgumentException(connectionId + ": guacamole parameter is not exist");
        }

        GuacamoleConnectParameterEntity existParameter = guacamoleConnectParameterEntities.get(0);
        if (existParameter.getParameterName().equals("password") && existParameter.getParameterValue().equals(guacamoleParameter.getOldValue())) {
            throw new IllegalArgumentException("guacamole parameter is not match");
        }
        guacamoleConnectParameterService.updateGuacamoleConnectParameter(guacamoleConnectParameterEntities.get(0), guacamoleParameter.getNewValue());
    }

    @Override
    public String getConnectHostname(String connectionId, String name) {
        List<GuacamoleConnectEntity> guacamoleConnectEntities = guacamoleConnectService.selectByInstanceId(connectionId);
        if (guacamoleConnectEntities==null || guacamoleConnectEntities.size()==0) {
            throw new IllegalArgumentException(connectionId + ": guacamole connect is not exist");
        }
        List<GuacamoleConnectParameterEntity> guacamoleConnectParameterEntities = guacamoleConnectParameterService.selectByConnectId(guacamoleConnectEntities.get(0).getConnectionId());

        guacamoleConnectParameterEntities = guacamoleConnectParameterEntities.stream().filter(p ->
                p.getParameterName().equals(name)).collect(Collectors.toList());

        if (guacamoleConnectParameterEntities==null || guacamoleConnectParameterEntities.size()==0) {
            throw new IllegalArgumentException(connectionId + ": guacamole parameter is not exist");
        }

        GuacamoleConnectParameterEntity existParameter = guacamoleConnectParameterEntities.get(0);

        return existParameter.getParameterValue();
    }
}
