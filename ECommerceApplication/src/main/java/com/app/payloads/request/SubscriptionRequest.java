package com.app.payloads.request;

import com.app.payloads.SubscriptionItemDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class SubscriptionRequest {
     private Long customerId;
        private LocalDate startDate;
        private LocalDate endDate;
        private List<SubscriptionItemDto> items; // List of items with quantity and frequency
        
}
