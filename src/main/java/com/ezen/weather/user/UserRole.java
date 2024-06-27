package com.ezen.weather.user;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ADMIN"), USER("USER");

    UserRole(String value) {
        this.value = value;
    }

    private String value;

    public static final UserRole DEFAULT = USER;
}

