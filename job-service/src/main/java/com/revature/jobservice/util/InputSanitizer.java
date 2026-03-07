package com.revature.jobservice.util;

public class InputSanitizer {

    private InputSanitizer() {
        // Private constructor to prevent instantiation
    }

    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }

        // Remove any potential XSS or SQL injection attempts
        String sanitized = input.trim();

        // Remove HTML tags
        sanitized = sanitized.replaceAll("<[^>]*>", "");

        // Remove script tags and content
        sanitized = sanitized.replaceAll("(?i)<script.*?>.*?</script>", "");

        // Remove SQL keywords (basic protection)
        sanitized = sanitized.replaceAll("(?i)(union|select|insert|update|delete|drop|create|alter|exec|execute|script|javascript|onerror|onload)\\s*[=\\(]", "");

        return sanitized;
    }

    public static String sanitizeAndTruncate(String input, int maxLength) {
        String sanitized = sanitize(input);
        if (sanitized != null && sanitized.length() > maxLength) {
            return sanitized.substring(0, maxLength);
        }
        return sanitized;
    }
}
