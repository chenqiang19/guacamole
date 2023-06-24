package com.ict.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ict.db.entity.GuacamoleConnectParameterEntity;

import java.util.List;

public interface GuacamoleConnectParameterService extends IService<GuacamoleConnectParameterEntity> {

    List<GuacamoleConnectParameterEntity> selectByConnectId(Long connectId);

    void updateGuacamoleConnectParameter(GuacamoleConnectParameterEntity guacamoleConnectParameterEntity, String newPasswd);
}
