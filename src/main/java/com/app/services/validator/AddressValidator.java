package com.app.services.validator;

import com.app.config.GlobalConfig;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressValidator {
  private final GlobalConfig globalConfig;

  // private List<String> validAddressKeys;
  @PostConstruct
  public void setConfig() {}

  public void validateAddress(Map<String, String> newDeliveryAddress) {
    if (newDeliveryAddress == null || newDeliveryAddress.isEmpty()) {
      throw new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "Address is required.");
    }
    List<String> validAddressKeys = globalConfig.getCustomerConfig().getAddressValidKeys();
    // Filter the input to only keep valid keys
    log.debug("Address entries before cleanup {}", newDeliveryAddress);
    var invalidKeys =
        newDeliveryAddress.keySet().stream()
            .filter(key -> !validAddressKeys.contains(key))
            .toList();
    log.debug("Address Invalid Keys : {}", invalidKeys);
    if (!invalidKeys.isEmpty()) {
      throw new APIException(
          APIErrorCode.BAD_REQUEST_RECEIVED,
          "Invalid address keys found " + invalidKeys + " and accepted keys " + validAddressKeys);
    }
    // return newDeliveryAddress.entrySet().stream()
    //   .filter(entry -> validAddressKeys.contains(entry.getKey()))
    // .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    // Update the filtered address in the database
  }

  public void validateServiceAreas(Map<String, Set<String>> serviceAreaMap) {
    if (serviceAreaMap == null || serviceAreaMap.isEmpty()) {
      throw new APIException(APIErrorCode.BAD_REQUEST_RECEIVED, "Service area is required.");
    }
    /*   Set<String> validKeys =  validKeys=Set.of("areas","zipcodes");
      if(validKeys == null){
        validKeys=Set.of("areas","zipcodes");
      } else {
          validKeys = globalConfig.getCustomerConfig().getServiceAreasKeys();
      }
        // Filter the input to only keep valid keys
      log.debug("Service are entries before cleanup {}", serviceAreaMap);
      var invalidKeys =
              serviceAreaMap.keySet().stream()
                      .filter(key -> !validKeys.contains(key))
                      .toList();
      log.debug("Service area Invalid Keys : {}", invalidKeys);
      if (!invalidKeys.isEmpty()) {
        throw new APIException(
                APIErrorCode.BAD_REQUEST_RECEIVED, "Invalid service area keys found "+ invalidKeys+" and accepted keys "+validKeys);
      }
    */
    // return serviceAreaMap.entrySet().stream()
    //   .filter(entry -> validKeys.contains(entry.getKey()))
    // .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    // Update the filtered address in the database
  }
}
