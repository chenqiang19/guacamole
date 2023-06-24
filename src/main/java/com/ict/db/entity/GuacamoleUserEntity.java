package com.ict.db.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@Data
@TableName("guacamole_user")
public class GuacamoleUserEntity implements Serializable {

    private static final long serialVersionUID = 410123401280888084L;

    @TableId
    private String userId;

    private Long entityId;

    private byte[] passwordHash;

    private byte[] passwordSalt;

    private Timestamp passwordDate;

    private Integer disabled;

    private Integer expired;

    private Time accessWindowStart;

    private Time accessWindowEnd;

    private Date validFrom;

    private Date validUntil;

    private String timezone;

    private String fullName;

    private String emailAddress;

    private String organization;

    private String organizationalRole;
}
