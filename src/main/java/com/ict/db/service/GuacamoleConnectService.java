package com.ict.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ict.db.entity.GuacamoleConnectEntity;

import java.util.List;

public interface GuacamoleConnectService extends IService<GuacamoleConnectEntity> {

    List<GuacamoleConnectEntity> selectByInstanceId(String instanceId);
}
