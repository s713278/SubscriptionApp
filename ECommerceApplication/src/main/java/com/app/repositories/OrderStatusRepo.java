package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.entites.OrderStatusHistory;

@Repository
public interface OrderStatusRepo extends JpaRepository<OrderStatusHistory, Long> {

}
