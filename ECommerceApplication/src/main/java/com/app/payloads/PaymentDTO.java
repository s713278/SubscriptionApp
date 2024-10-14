package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PaymentDTO {

    @Schema(description = "Creditcard Number", example = "XXXX-XXXX-XXXX-1280")
    @JsonProperty("credit_card")
    private String creditCardNum;

    @Schema(description = "Exp Date", example = "12/2030")
    @JsonProperty("expiry_date")
    private String expDate;

    @Schema(description = "Payment Method", example = "IN_STORE_PAYMENT")
    @JsonProperty("payment_method")
    private String paymentMethod;

    @JsonProperty("billing_address")
    private AddressDTO billingAddress;
}
