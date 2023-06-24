package com.ict.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GuacamoleEntityType {
    USER("USER", "USER"),
    USER_GROUP("USER_GROUP", "USER_GROUP");

    @EnumValue
    private String key;

    @JsonValue
    private String display;

    GuacamoleEntityType(String key, String display) {
        this.key = key;
        this.display = display;
    }

    public String getKey() { return this.key; }

    public String getDisplay() { return this.display; }
}
