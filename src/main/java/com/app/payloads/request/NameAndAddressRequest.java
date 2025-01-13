package com.app.payloads.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;

public record NameAndAddressRequest(
    @NotBlank(message = "Name is required.") @Schema(example = "Swamy Kunta") String name,
    @Schema(
            example =
                "{ \"country\": \"India\", "
                    + "\"type\": \"Home \", "
                    + "\"house_no\": \"MIG-3-973/L \", "
                    + "\"address1\": \"Road No-27F ,Mayurinagar\", "
                    + "\"address2\": \"Miyapur\", "
                    + "\"city\": \"Hyderabad\", "
                    + "\"state\": \"Telangana\", "
                    + "\"landmark\": \"Near Ramalayam Temple\", "
                    + "\"zipCode\": \"500049\", "
                    + "\"longitude\": \"909.90.9\", "
                    + "\"latitude\": \"129.90.9\", "
                    + "\"district\": \"RangaReddy\" }")
        Map<String, String> address) {}
