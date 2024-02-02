package com.app.payloads;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCredentials {

	@Schema(description = "Email", example = "swamy.kunta@gmail.com")
	@Email
	@Column(unique = true, nullable = false)
	private String email;

	@Schema(description = "Email", example = "12345678")
	private String password;
}
