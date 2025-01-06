package com.app.repositories.projections;

import com.app.entites.type.DeliveryMode;
import com.app.entites.type.SubFrequency;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface SKUSubscriptionPlanProjection {

  Map<String, Object> getEligibleDeliveryDays();

  SubFrequency getFrequency();

  DeliveryMode getDeliveryMode();
}
