package com.ict.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GuacamoleParameters {

    /* 裸机分配的fixed ip, 对应guacamole中的hostname */
    @JsonProperty("host_name")
    public String hostName;

    /* guacamole中的证书，默认为NULL */
    @JsonProperty("ignore_cert")
    public Boolean ignoreCert;

    /* 机器登陆的用户名，windows默认为administrator, linux默认为root */
    @JsonProperty("user_name")
    public String userName;

    /* 机器登陆的密码 */
    public String password;

    /* 远程登陆机器的端口，RDPm默认为3389，SSH默认为22 */
    public Integer port;

    /* 是否需要安全认证，默认为NULL */
    public String security;
}
