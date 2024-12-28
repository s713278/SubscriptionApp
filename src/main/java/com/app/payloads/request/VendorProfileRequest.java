package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Map;
import lombok.Data;

@Data
public class VendorProfileRequest implements Serializable {

  @NotBlank(message = "Business name is required.")
  @JsonProperty("business_name")
  private String businessName;

  @JsonProperty("description")
  private String description;

  @Schema(example = "Agriculture/Auto Mobiles")
  @NotBlank(message = "Business name is required.")
  @JsonProperty("business_type")
  private String businessType;

  @NotBlank(message = "Owner name is required.")
  @JsonProperty("owner_name")
  private String ownerName;

  @NotBlank(message = "Business contact person.")
  @JsonProperty("contact_person")
  private String contactPerson;

  @NotBlank(message = "Mobile number/User ID")
  @JsonProperty("contact_number")
  private String contactNumber;

  @JsonProperty("communication_email")
  private String communicationEmail;

  @Schema(
      example =
          "{ \"name\": \"Mirdoddi Fresh\", "
              + "\"address1\": \"123 Green Fields\", "
              + "\"address2\": \"Near Water Tank\", "
              + "\"city\": \"Mirdoddi\", "
              + "\"state\": \"Telangana\", "
              + "\"zipCode\": \"502108\", "
              + "\"district\": \"Siddipet\" }")
  @NotBlank(message = "Business location is required.")
  @JsonProperty("business_address")
  private Map<String, String> businessAddress;

  @Schema(
      example =
          "{ \"zipcode\": [\"502108\", \"502103\"], \"areas\": [\"Mirdoddi\", \"Siddipet\"] }")
  @NotBlank(message = "Service area(s) required.")
  @JsonProperty("service_areas")
  private Map<String, Object> serviceAreas;

  @JsonProperty("banner_image")
  private String bannerImage;

  @Schema(description = "This is to assign products later on.")
  @JsonProperty("assign_categories")
  private AssignCategoriesRequest assignCategories;
}
