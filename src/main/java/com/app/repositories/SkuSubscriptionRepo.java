package com.app.repositories;

import com.app.entites.SkuSubscription;
import com.app.entites.type.SubFrequency;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkuSubscriptionRepo extends JpaRepository<SkuSubscription, Long> {
  // Custom query methods can be added here if neededSub
  Optional<SkuSubscription> findBySkuIdAndSubscriptionPlanFrequency(
      Long skuId, SubFrequency frequency);
}
