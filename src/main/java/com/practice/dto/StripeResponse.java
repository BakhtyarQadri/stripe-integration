package com.practice.dto;

public record StripeResponse(String status, String message, String sessionId, String sessionUrl) {}