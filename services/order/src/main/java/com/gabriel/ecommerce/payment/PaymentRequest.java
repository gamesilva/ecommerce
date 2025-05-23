package com.gabriel.ecommerce.payment;

import com.gabriel.ecommerce.customer.CustomerResponse;
import com.gabriel.ecommerce.order.PaymentMethod;

import java.math.BigDecimal;

public record PaymentRequest(
        BigDecimal amount,
        PaymentMethod paymentMethod,
        Integer orderId,
        String orderReference,
        CustomerResponse customer
) {
}
