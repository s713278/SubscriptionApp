package com.app.repositories;

import com.app.entites.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

    Address findByCountryAndStateAndCityAndPincodeAndAddress1AndAddress2(String country, String state, String city,
            String pincode, String address1, String address2);
}
