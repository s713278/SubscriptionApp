package com.app.repositories;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.app.entites.Customer;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {

    @Query("SELECT u FROM Customer u WHERE u.id = ?1")
    List<Customer> findByAddress(Long userId);

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findByEmailOrMobile(String email, Long mobile);

    @Query("SELECT c FROM Customer c JOIN FETCH c.roles WHERE c.mobile = :mobile")
    Optional<Customer> findByMobile(@Param("mobile") Long mobile);

    @Query("SELECT c FROM Customer c WHERE c.mobile = :mobile")
    Optional<Customer> findUserByMobile(@Param("mobile") Long mobile);

    //Customer findByEmailActivationToken(String token);

    //Customer findByResetPasswordToken(String token);

    @Modifying
    @Query("UPDATE Customer u SET u.deliveryAddress = :newDeliveryAddress WHERE u.id = :userId")
    void updateDeliveryAddress(Long userId, Map<String, String> newDeliveryAddress);

    @Modifying
    @Query("UPDATE Customer u SET u.mobileVerified=:verified ,u.mobileVerifiedTime=CURRENT_TIMESTAMP  WHERE id=:userId")
    void updateMobileVerifiedStatus(Long userId, boolean verified);

    @Modifying
    @Query("UPDATE Customer u SET u.email=lower(:email) WHERE u.id=:userId")
    void updateEmail(Long userId, String email);

    @Modifying
    @Query("UPDATE Customer u SET u.deliveryInstructions=:map WHERE u.id=:userId")
    void updateDeliveryInstructions(Long userId, Map<String,String> map);

    @Modifying
    @Query("UPDATE Customer u SET u.firstName = :name , u.deliveryAddress = :address WHERE u.id = :userId")
    void addNameAddress(Long userId, String name, Map<String, String> address);

    static Specification<Customer> hasMobileNumber(Long mobileNumber) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("mobile"), mobileNumber);
    }

    @Query("SELECT COUNT(u) > 0 FROM Customer u WHERE u.mobile = :mobileNumber")
    boolean existsByMobile(Long mobileNumber);

}
