package com.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.entites.Order;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    // @Query("SELECT o FROM Order o WHERE o.customer.email = ?1 AND o.orderId =
    // ?2")
    // Order findOrderByEmailAndOrderId(String email, Long orderId);

    @Query("SELECT o FROM Order o WHERE o.vendorId = ?1")
    List<Order> findOrderByVendorId(Long vendorId);

    List<Order> findAllBySubscriptionCustomerEmail(String emailId);
}
