package com.badran.store.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.OffsetDateTime;

/**
 * Payment response returned after creating or completing payment workflows.
 */
@Data
@Schema(description = "Payment state and verification metadata for an order.")
public class PaymentDto {
    @Schema(description = "Payment identifier.", example = "3001")
    private Long paymentId;

    @Schema(description = "Order identifier associated with the payment.", example = "5001")
    private Long orderId;

    @Schema(description = "Payment method.", example = "cod")
    private String method;

    @Schema(description = "Payment status.", example = "paid")
    private String status;

    @Schema(description = "Generated transaction reference.", example = "TXN-A1B2C3D4")
    private String transactionRef;

    @Schema(description = "Receipt URL when an external receipt exists.", example = "https://example.com/receipts/TXN-A1B2C3D4")
    private String receiptUrl;

    @Schema(description = "Admin user that verified the payment, when applicable.", example = "1")
    private Long verifiedByUserId;

    @Schema(description = "Payment verification timestamp.", example = "2026-07-08T20:20:00+03:00")
    private OffsetDateTime verifiedAt;

    @Schema(description = "Payment creation timestamp.", example = "2026-07-08T20:15:30+03:00")
    private OffsetDateTime createdAt;
}
