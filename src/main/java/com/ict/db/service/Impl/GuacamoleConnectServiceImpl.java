package com.ict.db.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ict.db.dao.GuacamoleConnectDao;
import com.ict.db.entity.GuacamoleConnectEntity;
import com.ict.db.service.GuacamoleConnectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("guacamole_connect")
public class GuacamoleConnectServiceImpl extends ServiceImpl<GuacamoleConnectDao, GuacamoleConnectEntity> implements GuacamoleConnectService {

    @Resource
    private GuacamoleConnectDao guacamoleConnectDao;

    @Override
    public List<GuacamoleConnectEntity> selectByInstanceId(String instanceId) {
        QueryWrapper<GuacamoleConnectEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("connection_name", instanceId);
        return guacamoleConnectDao.selectList(wrapper);
    }
}
