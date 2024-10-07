package com.app.repositories;

import com.app.entites.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.customer.email = ?1 AND o.orderId = ?2")
    Order findOrderByEmailAndOrderId(String email, Long orderId);

    // @Query("SELECT o FROM Order o WHERE o.store.id = ?2")
    List<Order> findOrderByVendorId(Long storeId);

    List<Order> findAllByCustomerEmail(String emailId);
}
