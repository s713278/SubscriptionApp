package com.app.payloads.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class PauseSubscriptionRequest {
    
    @NotNull(message = "Subscription ID is required") private Long subscriptionId;

    @NotNull(message = "Pause Start Date is required") private LocalDate pauseStartDate;

    @NotNull(message = "Pause End Date is required") private LocalDate pauseEndDate;

    // Getters and Setters

    public Long getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(Long subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public LocalDate getPauseStartDate() {
        return pauseStartDate;
    }

    public void setPauseStartDate(LocalDate pauseStartDate) {
        this.pauseStartDate = pauseStartDate;
    }

    public LocalDate getPauseEndDate() {
        return pauseEndDate;
    }

    public void setPauseEndDate(LocalDate pauseEndDate) {
        this.pauseEndDate = pauseEndDate;
    }
}
