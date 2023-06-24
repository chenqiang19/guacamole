package com.ict.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ConnectionProxyEncryptionMethod {

    NONE(0, "NONE"),
    SSL(1, "SSL");

    @EnumValue
    private Integer key;

    @JsonValue
    private String display;

    ConnectionProxyEncryptionMethod(Integer key, String display) {
        this.key = key;
        this.display = display;
    }

    public Integer getKey() { return this.key; }

    public String getDisplay() { return this.display; }
}
