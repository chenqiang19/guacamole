package com.ict.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ict.constants.GuacamoleEntityType;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("guacamole_entity")
public class GuacamoleEntity implements Serializable {

    private static final long serialVersionUID = 6543064068429188571L;

    @TableId
    private Long entityId;

    private String name;

    @TableField("type")
    private GuacamoleEntityType type;

}
