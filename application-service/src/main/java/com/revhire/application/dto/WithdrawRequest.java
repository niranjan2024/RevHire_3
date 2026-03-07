package com.revhire.application.dto;

import jakarta.validation.constraints.Size;

public class WithdrawRequest {

    @Size(max = 250, message = "Withdraw reason must not exceed 250 characters")
    private String reason;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
