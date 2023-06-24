package com.ict.domain;

import lombok.Data;

@Data
public class GuacamoleParameter {

    /* 参数名称 */
    private String parameterName;

    /* 参数新值 */
    private String newValue;

    /* 参数旧值 */
    private String oldValue;
}
