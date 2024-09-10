package com.app.repositories;

import com.app.entites.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<Customer, Long> {

    @Query("SELECT u FROM Customer u WHERE u.userId = ?1")
    List<Customer> findByAddress(Long userId);

    Optional<Customer> findByEmailIgnoreCase(String email);
}
