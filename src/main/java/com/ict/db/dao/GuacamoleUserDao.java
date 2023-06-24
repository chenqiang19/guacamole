package com.ict.db.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ict.db.entity.GuacamoleUserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GuacamoleUserDao extends BaseMapper<GuacamoleUserEntity> {
}
