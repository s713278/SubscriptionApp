package com.app.repositories;

import com.app.entites.Customer;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    @Query("SELECT u FROM Customer u WHERE u.id = ?1")
    List<Customer> findByAddress(Long userId);

    Optional<Customer> findByEmail(String email);
    
    Optional<Customer> findByEmailOrMobile(String email,Long mobile);

    @Query("SELECT c FROM Customer c JOIN FETCH c.roles WHERE c.mobile = :mobile")
    Optional<Customer> findByMobile(@Param("mobile")Long mobile);
    
    Customer findByEmailActivationToken(String token);
    Customer findByResetPasswordToken(String token);
    
    @Modifying
    @Query("UPDATE Customer u SET u.deliveryAddress = :newDeliveryAddress WHERE u.id = :userId")
    void updateDeliveryAddress(Long userId, Map<String, String> newDeliveryAddress);

}
