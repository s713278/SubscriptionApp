package com.app.payloads.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import lombok.Data;

@Data
public class VendorProfileRequest implements Serializable {

  @Schema(example = "Mithra Organic Farm")
  @NotBlank(message = "Business name is required.")
  @JsonProperty("business_name")
  private String businessName;

  @Schema(example = "Mithra Organic Products")
  @JsonProperty("description")
  private String description;

  @Schema(example = "Agriculture")
  @NotBlank(message = "Business name is required.")
  @JsonProperty("business_type")
  private String businessType;

  @Schema(example = "Group Of NSR")
  @NotBlank(message = "Owner name is required.")
  @JsonProperty("owner_name")
  private String ownerName;

  @Schema(example = "SKunta")
  @NotBlank(message = "Business contact person.")
  @JsonProperty("contact_person")
  private String contactPerson;

  @Pattern(
      regexp = "^[6-9]\\d{9}$",
      message = "Mobile number must be 10 digits and start with 6, 7, 8, or 9.")
  @Schema(example = "9912149001")
  @NotBlank(message = "Mobile number/User ID")
  @JsonProperty("contact_number")
  private String contactNumber;

  @JsonProperty("communication_email")
  private String communicationEmail;

  @Schema(
      example =
          "{ \"country\": \"India\", "
              + "\"address1\": \"Survey No#190,900 \", "
              + "\"address2\": \"Kasulabad\", "
              + "\"city\": \"Mirdoddi\", "
              + "\"state\": \"Telangana\", "
              + "\"zipCode\": \"502108\", "
              + "\"district\": \"Siddipet\" }")
  // @NotBlank(message = "Business location is required.")
  @JsonProperty("business_address")
  private Map<String, String> businessAddress;

  @Schema(
      example =
          "{ \"zipcode\": [\"502108\", \"502103\"], \"areas\": [\"Mirdoddi\", \"Siddipet\"] }")
  // @NotBlank(message = "Service area(s) required.")
  @JsonProperty("service_areas")
  private Map<String, Set<String>> serviceAreas;

  @JsonProperty("banner_image")
  private String bannerImage;

  @NotNull(message = "Categories is required.")
  @Schema(description = "This is to assign products later on.")
  @JsonProperty("assign_categories")
  private AssignCategoriesRequest assignCategories;
}
