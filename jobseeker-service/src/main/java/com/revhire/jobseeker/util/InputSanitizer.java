package com.revhire.jobseeker.util;

import com.revhire.jobseeker.exception.BadRequestException;

public final class InputSanitizer {

    private InputSanitizer() {
    }

    public static String sanitize(String value, String fieldName) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            return null;
        }
        String lower = trimmed.toLowerCase();
        if (lower.contains("<script") || lower.contains("<iframe") || lower.contains("onload=")) {
            throw new BadRequestException(fieldName + " contains unsafe content");
        }
        return trimmed;
    }

    public static String require(String value, String fieldName) {
        String sanitized = sanitize(value, fieldName);
        if (sanitized == null) {
            throw new BadRequestException(fieldName + " is required");
        }
        return sanitized;
    }
}
