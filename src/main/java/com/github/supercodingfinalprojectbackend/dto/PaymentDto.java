package com.github.supercodingfinalprojectbackend.dto;

import com.github.supercodingfinalprojectbackend.entity.Payment;
import lombok.*;

public class PaymentDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString
    public static class PaymentIdResponse {
        private Long paymentId;

        public static PaymentIdResponse from(Payment payment) {
            return new PaymentIdResponse(payment.getPaymentId());
        }
    }
}
