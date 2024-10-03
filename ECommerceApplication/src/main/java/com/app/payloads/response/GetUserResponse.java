package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GetUserResponse(Boolean success, Data data) {

    public static record Data(@JsonProperty("user_id") Long id, String name, String email, Long mobile) {

    }
}
