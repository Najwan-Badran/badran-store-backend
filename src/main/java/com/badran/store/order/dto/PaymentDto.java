package com.badran.store.order.dto;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class PaymentDto {
    private Long paymentId;
    private Long orderId;
    private String method;
    private String status;
    private String transactionRef;
    private String receiptUrl;
    private Long verifiedByUserId;
    private OffsetDateTime verifiedAt;
    private OffsetDateTime createdAt;
}
