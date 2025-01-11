package com.app.payloads.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class ServiceSubscriptionResponse extends SubscriptionCreateResponse {

  @JsonProperty("start_date")
  private LocalDate startDate;

  @JsonProperty("service_validity")
  private Integer serviceValidity;

  @JsonProperty("expiration_date")
  private LocalDate expirationDate;

  @JsonProperty("no_of_uses")
  private Integer noOfUses;

  @JsonProperty("service_location")
  private Map<String, String> serviceLocation;
}
