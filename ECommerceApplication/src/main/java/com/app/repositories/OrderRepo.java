package com.app.repositories;

import com.app.entites.Order;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.email = ?1 AND o.id = ?2")
    Order findOrderByEmailAndOrderId(String email, Long cartId);
    
   // @Query("SELECT o FROM Order o WHERE o.store.id = ?2")
    List<Order> findOrderByStoreId(Long storeId);
    
    List<Order> findAllByEmail(String emailId);
}
