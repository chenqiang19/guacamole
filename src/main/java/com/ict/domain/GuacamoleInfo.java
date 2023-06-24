package com.ict.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GuacamoleInfo {

    /* 项目名称，对应guacamole中的group name */
    @JsonProperty("project_name")
    public String projectName;

    /* 实例名称，对应guacamole中的connection name */
    @JsonProperty("instance_name")
    public String instanceName;

    /* openstack user id，对应guacamole中的user id */
    @JsonProperty("user_id")
    public String userId;

    /* openstack user password，对应guacamole中的user password */
    @JsonProperty("user_password")
    public String userPassword;

    /* guacamole中的连接协议 windows-RDP linux-SSH */
    public String protocol;

    /* guacamole参数 */
    @JsonProperty("guacamole_parameters")
    public GuacamoleParameters guacamoleParameters;

}
