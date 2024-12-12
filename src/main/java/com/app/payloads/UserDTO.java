package com.app.payloads;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public  record UserDTO(@JsonProperty("user_id") Long id, String name, String email, String mobile, @JsonProperty("delivery_address") Map<String,String> deliveryAddress) {

    }