package com.badran.store.enums;

/**
 * Payment statuses persisted as string values in the existing payments table.
 */
public enum PaymentStatus {
    UNPAID("unpaid"),
    PENDING_VERIFICATION("pending_verification"),
    PAID("paid"),
    REJECTED("rejected");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    /**
     * Returns the database value for this status.
     */
    public String value() {
        return value;
    }
}
