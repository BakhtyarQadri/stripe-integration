package com.practice.dto;

public record PaymentRequest(String name, Long quantity, Long amount, String currency) {}
