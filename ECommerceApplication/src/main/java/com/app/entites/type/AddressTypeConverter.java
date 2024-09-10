package com.app.entites.type;
import com.app.payloads.AddressDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.IOException;

@Converter(autoApply = true)
public class AddressTypeConverter implements AttributeConverter<AddressDTO, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(AddressDTO attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting address to JSON string", e);
        }
    }

    @Override
    public AddressDTO convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, AddressDTO.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON string to address", e);
        }
    }
}
