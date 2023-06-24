package com.ict.service;

import com.ict.domain.GuacamoleInfo;
import com.ict.domain.GuacamoleParameter;

import java.security.NoSuchAlgorithmException;

public interface GuacamoleService {

    void insertGuacamoleInfo(GuacamoleInfo guacameloInfo) throws NoSuchAlgorithmException;

    void modifiedParameter(GuacamoleParameter resources, String connectionId);

    String getConnectHostname(String connectionId, String name);
}
