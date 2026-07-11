package com.badran.store.enums;

/**
 * User roles persisted as string values in the existing roles table.
 */
public enum UserRole {
    ADMIN("admin"),
    CUSTOMER("customer");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    /**
     * Returns the database value for this role.
     */
    public String value() {
        return value;
    }
}
