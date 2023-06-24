package com.ict.db.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ict.constants.ConnectionProxyEncryptionMethod;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("guacamole_connection")
public class GuacamoleConnectEntity implements Serializable {
    private static final long serialVersionUID = -8359653997608117134L;

    @TableId
    private Long connectionId;

    private String connectionName;

    private Long parentId;

    private String protocol;

    private Long proxyPort;

    private String proxyHostname;

    @TableField("proxy_encryption_method")
    private ConnectionProxyEncryptionMethod proxyEncryptionMethod;

    private Long maxConnections;

    private Long maxConnectionsPerUser;

    private Integer failoverOnly;
}
