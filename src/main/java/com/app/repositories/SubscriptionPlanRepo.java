package com.app.repositories;

import com.app.entites.SubscriptionPlan;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepo extends CrudRepository<SubscriptionPlan, Long> {

  @Query("SELECT sp FROM SubscriptionPlan sp WHERE sp.id IN :ids ")
  List<SubscriptionPlan> findByIds(Set<Long> ids);
}
