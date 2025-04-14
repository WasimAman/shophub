package com.shophub_backend.model;

import com.shophub_backend.enums.PaymentMethod;
import com.shophub_backend.enums.PaymentStatus;

import lombok.Data;

@Data
public class PaymentDetails {
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String razorpayUrl;
    private String razorpayUrlReferencId;
    private String razorpayPaymentStatus;
    private String razorpayPaymentId;
}
