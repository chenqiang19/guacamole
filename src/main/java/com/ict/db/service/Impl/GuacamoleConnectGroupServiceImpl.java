package com.ict.db.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ict.db.dao.GuacamoleConnectGroupDao;
import com.ict.db.entity.GuacamoleConnectGroupEntity;
import com.ict.db.service.GuacamoleConnectGroupService;
import org.springframework.stereotype.Service;

@Service("guacamole_connect_group")
public class GuacamoleConnectGroupServiceImpl extends ServiceImpl<GuacamoleConnectGroupDao, GuacamoleConnectGroupEntity> implements GuacamoleConnectGroupService {
}
