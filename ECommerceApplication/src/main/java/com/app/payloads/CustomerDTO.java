package com.app.payloads;

import java.util.HashSet;
import java.util.Set;

import com.app.entites.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Customer Registration Request")
public class CustomerDTO {

    @JsonIgnore
    private Long userId;

    @NotBlank(message = "First name is required.")
    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("mobile")
    private Long mobile;

    // @NotBlank(message = "Email is required.")
    @JsonProperty("email")
    private String email;

    @NotBlank(message = "Password is required.")
    private String password;

    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @JsonProperty("delivery_address")
    private AddressDTO deliveryAddress;

    @JsonIgnore
    private CartDTO cart;
}
