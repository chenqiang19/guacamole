package com.ict.db.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ict.db.dao.GuacamoleUserDao;
import com.ict.db.entity.GuacamoleUserEntity;
import com.ict.db.service.GuacamoleUserService;
import org.springframework.stereotype.Service;

@Service("guacamole_user")
public class GuacamoleUserServiceImpl extends ServiceImpl<GuacamoleUserDao, GuacamoleUserEntity> implements GuacamoleUserService {
}
