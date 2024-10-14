package com.app.payloads;

import com.fasterxml.jackson.annotation.JsonProperty;

public  record UserDTO(@JsonProperty("user_id") Long id, String name, String email, Long mobile) {

    }