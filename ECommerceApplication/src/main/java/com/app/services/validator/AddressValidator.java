package com.app.services.validator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.app.config.GlobalConfig;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressValidator {

	private final GlobalConfig globalConfig;
	private List<String> validAddressKyes;

	@PostConstruct
	public void setConfig() {
		this.validAddressKyes = globalConfig.getCustomerConfig().getAddressValidKeys();
	}

	public Map<String, String> validateAddress(Long userId, Map<String, String> newDeliveryAddress) {
		if (newDeliveryAddress == null || newDeliveryAddress.isEmpty()) {
			throw new APIException(APIErrorCode.API_400, "Address map is empty/null!!");
		}
		// Filter the input to only keep valid keys
		log.debug("Addres entries before cleanup {}", newDeliveryAddress);
		var invalidKeys = newDeliveryAddress.keySet().stream().filter(key -> !validAddressKyes.contains(key)).toList();
		if (!invalidKeys.isEmpty()) {
			throw new APIException(APIErrorCode.API_400, "Invalid address kyes found ", invalidKeys);
		}
		return newDeliveryAddress.entrySet().stream().filter(entry -> validAddressKyes.contains(entry.getKey()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		// Update the filtered address in the database
	}
}
