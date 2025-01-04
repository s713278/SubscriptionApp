package com.app.payloads.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record NameAndAddressRequest(
    @NotBlank(message = "Name is required.") @Schema(example = "Swamy K") String name,
    @Schema(
            example =
                "{ \"country\": \"India\", "
                    + "\"name\": \"Swamy K \", "
                    + "\"address1\": \"Survey No#190,900 \", "
                    + "\"address2\": \"Kasulabad\", "
                    + "\"city\": \"Mirdoddi\", "
                    + "\"state\": \"Telangana\", "
                    + "\"zipCode\": \"502108\", "
                    + "\"longitude\": \"909.90.9\", "
                    + "\"latitude\": \"129.90.9\", "
                    + "\"district\": \"Siddipet\" }")
        Map<String, String> address) {}
