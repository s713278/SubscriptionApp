package com.app.entites.type;
import com.app.payloads.AddressDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Converter
@Slf4j
public class AddressTypeConverter implements AttributeConverter<AddressDTO, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AddressDTO attribute) {
        try {
        	log.debug("AddressDTO {}",attribute);
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
        	log.error("Unable to creating into JSON string for address DTO {}",attribute,e);
            throw new IllegalArgumentException("Error converting address to JSON string", e);
        }
    }

    @Override
    public AddressDTO convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, AddressDTO.class);
        } catch (IOException e) {
        	log.error("Unable to creating into JSON Object for address string {}",dbData,e);
            throw new IllegalArgumentException("Error converting JSON string to address", e);
        }
    }
}
