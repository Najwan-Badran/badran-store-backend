package com.badran.store.common;

/**
 * Shared domain values persisted in the existing database schema.
 */
public final class DomainConstants {

    private DomainConstants() {
    }

    /**
     * User role names.
     */
    public static final class Roles {
        public static final String ADMIN = "admin";
        public static final String CUSTOMER = "customer";

        private Roles() {
        }
    }

    /**
     * Order lifecycle statuses.
     */
    public static final class OrderStatus {
        public static final String PENDING = "pending";
        public static final String PROCESSING = "processing";

        private OrderStatus() {
        }
    }

    /**
     * Payment statuses stored in the payments table.
     */
    public static final class PaymentStatus {
        public static final String UNPAID = "unpaid";
        public static final String PENDING_VERIFICATION = "pending_verification";
        public static final String PAID = "paid";

        private PaymentStatus() {
        }
    }

    /**
     * Supported fulfillment methods.
     */
    public static final class FulfillmentMethod {
        public static final String DELIVERY_ALIAS = "delivery";
        public static final String HOME_DELIVERY = "home_delivery";
        public static final String PICKUP = "pickup";

        private FulfillmentMethod() {
        }
    }

    /**
     * Supported payment methods.
     */
    public static final class PaymentMethod {
        public static final String CASH_ALIAS = "cash";
        public static final String COD = "cod";
        public static final String CARD = "card";
        public static final String BANK_TRANSFER = "bank_transfer";

        private PaymentMethod() {
        }
    }

    /**
     * Review moderation statuses.
     */
    public static final class ReviewStatus {
        public static final String PUBLISHED = "published";

        private ReviewStatus() {
        }
    }
}
