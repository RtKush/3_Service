package com.demo.notification.dto;
import jakarta.validation.constraints.NotBlank;
public record NotificationRequest(
        Long userId,
        String sku,
        @NotBlank String message,
        String type,
        String title,
        String severity,
        String link) {}
