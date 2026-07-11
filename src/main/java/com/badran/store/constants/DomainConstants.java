package com.badran.store.constants;

/**
 * Shared domain string constants used where persisted values must remain stable.
 */
public final class DomainConstants {
    private DomainConstants() {
    }

    /**
     * Supported fulfillment method values and accepted aliases.
     */
    public static final class FulfillmentMethod {
        public static final String DELIVERY_ALIAS = "delivery";
        public static final String HOME_DELIVERY = "home_delivery";
        public static final String PICKUP = "pickup";

        private FulfillmentMethod() {
        }
    }

    /**
     * Supported payment method values and accepted aliases.
     */
    public static final class PaymentMethod {
        public static final String CASH_ALIAS = "cash";
        public static final String COD = "cod";
        public static final String CARD = "card";
        public static final String BANK_TRANSFER = "bank_transfer";

        private PaymentMethod() {
        }
    }
}
