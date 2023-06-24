package com.ict.db.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ict.db.dao.GuacamoleConnectParameterDao;
import com.ict.db.entity.GuacamoleConnectParameterEntity;
import com.ict.db.service.GuacamoleConnectParameterService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("guacamole_connect_parameter")
public class GuacamoleConnectParameterServiceImpl extends ServiceImpl<GuacamoleConnectParameterDao, GuacamoleConnectParameterEntity> implements GuacamoleConnectParameterService {

    @Resource
    private GuacamoleConnectParameterDao guacamoleConnectParameterDao;

    @Override
    public List<GuacamoleConnectParameterEntity> selectByConnectId(Long connectId) {
        QueryWrapper<GuacamoleConnectParameterEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("connection_id", connectId);
        return guacamoleConnectParameterDao.selectList(wrapper);
    }

    @Override
    public void updateGuacamoleConnectParameter(GuacamoleConnectParameterEntity guacamoleConnectParameterEntity, String newPasswd) {
        UpdateWrapper<GuacamoleConnectParameterEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("connection_id", guacamoleConnectParameterEntity.getConnectionId());
        updateWrapper.eq("parameter_name", guacamoleConnectParameterEntity.getParameterName());

        guacamoleConnectParameterEntity.setParameterValue(newPasswd);
        guacamoleConnectParameterDao.update(guacamoleConnectParameterEntity, updateWrapper);
        return;
    }
}
