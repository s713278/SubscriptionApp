package com.app.payloads;

import lombok.Data;

@Data
public class AddressDTO {

    private String address1;
    private String address2;
    private String city;
    private String state;
    private String country;
    private String pincode;
}
