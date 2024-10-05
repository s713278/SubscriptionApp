package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entites.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

}
