package com.app.payloads.response;

import com.app.entites.type.OrderStatus;
import com.app.services.constants.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OrderUpdateResponse {

    @JsonProperty("order_id")
    private Long id;

    @JsonProperty("old_satus")
    private OrderStatus oldStatus;

    @JsonProperty("new_staus")
    private OrderStatus newStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeFormat.DATE_TIME_FORMAT, timezone = DateTimeFormat.TIME_ZONE)
    @JsonProperty("timestamp")
    private LocalDateTime changedAt;
}
