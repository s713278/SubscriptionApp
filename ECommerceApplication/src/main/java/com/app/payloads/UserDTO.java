package com.app.payloads;

import com.app.entites.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "User Registration Request Body")
public class UserDTO {

  @JsonIgnore private Long userId;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @JsonProperty("mobile_phone")
  private String mobileNumber;

  @NotBlank(message = "Email is required.")
  @JsonProperty("email")
  private String email;

  @NotBlank(message = "Password is required.")
  private String password;

  @JsonIgnore private Set<Role> roles = new HashSet<>();

  @JsonIgnore private AddressDTO address;

  @JsonIgnore private CartDTO cart;
}
