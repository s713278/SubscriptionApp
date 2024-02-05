package com.app.payloads.request;

import com.app.services.constants.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderUpdateRequest {
    String newSatus;

    OrderStatus oldStatus;
}
