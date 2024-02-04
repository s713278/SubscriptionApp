package com.app.payloads;

import lombok.Data;

@Data
public class ShippingDTO {

  private String shippingMethod;

  private AddressDTO address;
}
