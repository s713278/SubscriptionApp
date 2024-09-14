package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public class SignInResponse {

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("user_token")
    @Schema(description = "User Auth Token", example = "sdfasdAAASDF.SDFSDFSAFADFSFD.JKOJIJH")
    private String userToken;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

}
