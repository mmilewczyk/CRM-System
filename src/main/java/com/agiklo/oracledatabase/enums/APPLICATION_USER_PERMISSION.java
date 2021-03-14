package com.agiklo.oracledatabase.enums;

import lombok.Getter;

@Getter
public enum APPLICATION_USER_PERMISSION {

    EMPLOYEE_READ("employee:read"),
    EMPLOYEE_WRITE("employee:write"),
    CUSTOMER_READ("customer:read"),
    CUSTOMER_WRITE("customer:write");

    private final String permission;

    APPLICATION_USER_PERMISSION(String permission) {
        this.permission = permission;
    }
}
