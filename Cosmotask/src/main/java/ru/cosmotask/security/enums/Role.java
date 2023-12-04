package ru.cosmotask.security.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    STAR("ROLE_STAR"),
    SUPER_STAR("ROLE_SUPER_STAR");

    final String role;

    Role(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return role;
    }
}
