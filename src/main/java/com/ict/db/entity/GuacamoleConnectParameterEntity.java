package com.ict.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("guacamole_connection_parameter")
public class GuacamoleConnectParameterEntity implements Serializable {

    private static final long serialVersionUID = -6047373327767026460L;

    @TableId(value = "connection_id", type = IdType.INPUT)
    private Long connectionId;

    private String parameterName;

    private String parameterValue;
}
