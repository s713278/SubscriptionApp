package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public  record UserDTO(@JsonProperty("user_id") Long id, String name, String email, String mobile, @JsonProperty("delivery_address") Map<String,String> deliveryAddress) {

    }