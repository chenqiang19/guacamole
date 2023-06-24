package com.ict.db.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ict.constants.GroupType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("guacamole_connection_group")
public class GuacamoleConnectGroupEntity implements Serializable {

    private static final long serialVersionUID = 2313737747351613569L;

    @TableId
    private Long connectionGroupId;

    private Long parentId;

    private String connectionGroupName;

    @TableField("type")
    private GroupType type;

    private Long maxConnections;

    private Long maxConnectionsPerUser;

    private Integer enableSessionAffinity;
}
