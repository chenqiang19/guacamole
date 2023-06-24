package com.ict.db.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ict.db.dao.GuacamoleEntityDao;
import com.ict.db.entity.GuacamoleEntity;
import com.ict.db.service.GuacamoleEntityService;
import org.springframework.stereotype.Service;

@Service("guacamole_entity")
public class GuacamoleEntityServiceImpl extends ServiceImpl<GuacamoleEntityDao, GuacamoleEntity> implements GuacamoleEntityService {
}
