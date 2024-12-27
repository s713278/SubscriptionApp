package com.app.payloads.response;

import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.VendorStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;

@Data
public class VendorProfileResponse implements Serializable {

  @JsonProperty("vendor_id")
  private Long id;

  @JsonProperty("business_name")
  private String businessName;

  @JsonProperty("description")
  private String description;

  @JsonProperty("business_type")
  private String businessType;

  @JsonProperty("owner_name")
  private String ownerName;

  @JsonProperty("contact_person")
  private String contactPerson;

  @JsonProperty("contact_number")
  private String contactNumber;

  @JsonProperty("communication_email")
  private String communicationEmail;

  @JsonProperty("business_address")
  private Map<String, String> businessAddress;

  @JsonProperty("service_areas")
  private Map<String, Object> serviceAreas;

  @JsonProperty("banner_image")
  private String bannerImage;

  @JsonProperty("user_id")
  private Long userId;

  @JsonProperty("vendor_status")
  private VendorStatus status;

  @JsonProperty("approval_status")
  private ApprovalStatus approvalStatus;

  @JsonProperty("gst_number")
  private String GSTNumber;

  @JsonProperty("pan_number")
  private String PANNumber;

  @JsonProperty("reg_number")
  private String regNumber;

  @JsonProperty("created_date")
  private LocalDateTime createdDate;

  @JsonProperty("created_by")
  private String createdBy;
}
