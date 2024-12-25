package com.app.payloads.response;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AuthDetailsDTO{

    @JsonProperty("user_id")
    private Long userId;
   
    //Short Lived Token
    @JsonProperty("access_token")
    @Schema(description = "Short lived access token", example = "sdfasdAAASDF.SDFSDFSAFADFSFD.JKOJIJH")
    private String userToken;

    @JsonProperty("name")
    private String firstName;

    @JsonProperty("mobile_verified")
    boolean mobileVerified;

    @JsonProperty("email_verified")
    boolean emailVerified;
    //Long Lived Token
    @JsonProperty("refresh_token")
    @Schema(description = "Long lived refresh Token", example = "sdfasdAAASDF.SDFSDFSAFADFSFD.JKOJIJH")
    private String refreshToken;

    private String message;

    @JsonProperty("referred_vendor_id")
    private Long referredVendorId;

    private Map<String,String> address;

    private Set<String> roles;
}
