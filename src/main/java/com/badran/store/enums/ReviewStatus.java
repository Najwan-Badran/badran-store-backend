package com.badran.store.enums;

/**
 * Review moderation statuses persisted as string values in the existing reviews table.
 */
public enum ReviewStatus {
    PENDING_MODERATION("pending_moderation"),
    PUBLISHED("published"),
    REJECTED("rejected");

    private final String value;

    ReviewStatus(String value) {
        this.value = value;
    }

    /**
     * Returns the database value for this status.
     */
    public String value() {
        return value;
    }
}
