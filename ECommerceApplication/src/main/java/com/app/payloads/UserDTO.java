package com.app.payloads;

import java.util.HashSet;
import java.util.Set;

import com.app.entites.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	@JsonIgnore
	private Long userId;
	private String firstName;
	private String lastName;
	private String mobileNumber;
	private String email;
	private String password;

	@JsonIgnore
	private Set<Role> roles = new HashSet<>();

	@JsonIgnore
	private AddressDTO address;

	@JsonIgnore
	private CartDTO cart;
}
