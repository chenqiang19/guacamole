package com.ict.constants;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GroupType {

    ORGANIZATIONAL(0, "ORGANIZATIONAL"),
    BALANCING(1, "BALANCING");

    @EnumValue
    private Integer key;

    @JsonValue
    private String display;

    GroupType(int key, String display) {
        this.key = key;
        this.display = display;
    }

    public Integer getKey() { return this.key; }

    public String getDisplay() { return this.display; }
}
