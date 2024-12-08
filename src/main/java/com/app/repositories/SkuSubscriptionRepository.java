package com.app.repositories;

import com.app.entites.SkuSubscription;
import com.app.entites.type.SubFrequency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SkuSubscriptionRepository extends JpaRepository<SkuSubscription, Long> {
    // Custom query methods can be added here if neededSub
    Optional<SkuSubscription> findBySkuIdAndSubscriptionPlanFrequency(Long skuId, SubFrequency frequency);
}
