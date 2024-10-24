package com.app.payloads.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public class AuthDetailsDTO {

    @JsonProperty("user_id")
    private Long userId;
   
    //Short Lived Token
    @JsonProperty("access_token")
    @Schema(description = "Short lived access token", example = "sdfasdAAASDF.SDFSDFSAFADFSFD.JKOJIJH")
    private String userToken;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;
    
    //Long Lived Token
    @JsonProperty("refresh_token")
    @Schema(description = "Long lived refresh Token", example = "sdfasdAAASDF.SDFSDFSAFADFSFD.JKOJIJH")
    private String refreshToken;

    private String message;

    @JsonProperty("active_subscriptions")
    private List<?> activeSubscriptions;
}
