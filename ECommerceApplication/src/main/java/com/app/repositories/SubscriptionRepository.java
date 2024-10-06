package com.app.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.entites.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

	@Query("Select id,quantity,frequency,status,fromStartDate,nextDeliveryDate from Subscription s where s.id=?1")
	Optional<Subscription> fetchSubscription(final Long subId);
}
