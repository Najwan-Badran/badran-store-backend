package com.badran.store.enums;

/**
 * Order lifecycle statuses persisted as string values in the existing orders table.
 */
public enum OrderStatus {
    PENDING("pending"),
    PENDING_VERIFICATION("pending_verification"),
    CONFIRMED("confirmed"),
    PROCESSING("processing"),
    OUT_FOR_DELIVERY("out_for_delivery"),
    READY_FOR_PICKUP("ready_for_pickup"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    /**
     * Returns the database value for this status.
     */
    public String value() {
        return value;
    }
}
