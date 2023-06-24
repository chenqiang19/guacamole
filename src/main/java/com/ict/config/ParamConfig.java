package com.ict.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
/**
 * 
* Title: MyConfig
* Description:
* 自定义配置文件 
* Version:1.0.0
* @date 2018年1月20日
 */
@Component
public class ParamConfig {

    @Value("${guacd.ip}")
    private String guacdIp;

    public String getGuacdIp() {
        return guacdIp;
    }

    @Value("${guacd.port:4822}")
    private Integer guacdPort;

    public Integer getGuacdPort() {
        return guacdPort;
    }

}
