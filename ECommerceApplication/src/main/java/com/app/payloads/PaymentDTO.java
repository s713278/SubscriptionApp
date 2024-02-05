package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PaymentDTO {

    @JsonProperty("credit_card")
    private String creditCardNum;

    @JsonProperty("expiry_date")
    private String expDate;

    private String paymentMethod;

    @JsonProperty("billing_address")
    private AddressDTO billingAddress;
}
