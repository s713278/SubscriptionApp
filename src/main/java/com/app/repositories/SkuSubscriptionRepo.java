package com.app.repositories;

import com.app.entites.SkuSubscriptionPlan;
import com.app.entites.type.SubFrequency;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkuSubscriptionRepo extends JpaRepository<SkuSubscriptionPlan, Long> {
  // Custom query methods can be added here if neededSub
  Optional<SkuSubscriptionPlan> findBySkuIdAndSubscriptionPlanFrequency(
      Long skuId, SubFrequency frequency);
}
