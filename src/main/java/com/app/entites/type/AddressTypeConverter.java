package com.app.entites.type;

import com.app.payloads.AddressDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class AddressTypeConverter implements TypeConverter<AddressDTO, Map<String, String>> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, String> toEntityType(AddressDTO addressDTO) {
        log.debug("AddressDTO {}", addressDTO);
        if (addressDTO == null) {
            return Map.of();
        }
        Map<String, String> map = objectMapper.convertValue(addressDTO, new TypeReference<Map<String, String>>() {
        });
        return map;
    }

    @Override
    public AddressDTO fromEntityType(Map<String, String> dbData) {
        if (dbData == null) {
            return new AddressDTO();
        }
        AddressDTO addressData = objectMapper.convertValue(dbData, new TypeReference<AddressDTO>() {
        });
        return addressData;
    }
}
