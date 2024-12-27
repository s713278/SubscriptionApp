package com.app.repositories;

import com.app.entites.Order;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

  // @Query("SELECT o FROM Order o WHERE o.customer.email = ?1 AND o.orderId =
  // ?2")
  // Order findOrderByEmailAndOrderId(String email, Long orderId);

  @Query("SELECT o FROM Order o WHERE o.vendorId = ?1")
  List<Order> findOrderByVendorId(Long vendorId);

  List<Order> findAllBySubscriptionUserId(final Long userId);

  @Query(
      "SELECT o FROM Order o WHERE o.subscription.userId = :userId AND o.deliveryDate BETWEEN :startDate AND :endDate")
  List<Order> findBySubscriptionUserIdAndDateRange(
      @Param("userId") Long userId,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate);
}
