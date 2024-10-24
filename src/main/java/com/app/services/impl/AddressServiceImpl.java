package com.app.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entites.Address;
import com.app.entites.Customer;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.payloads.AddressDTO;
import com.app.repositories.AddressRepo;
import com.app.repositories.CustomerRepo;
import com.app.services.AddressService;

@Transactional
@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private CustomerRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO) {

        String country = addressDTO.getCountry();
        String state = addressDTO.getState();
        String city = addressDTO.getCity();
        String pincode = addressDTO.getPincode();
        String street = addressDTO.getAddress1();
        String buildingName = addressDTO.getAddress2();

        Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndAddress1AndAddress2(country, state,
                city, pincode, street, buildingName);

        if (addressFromDB != null) {
            throw new APIException(APIErrorCode.API_400,"Address already exists with addressId: " + addressFromDB.getAddressId());
        }

        Address address = modelMapper.map(addressDTO, Address.class);

        Address savedAddress = addressRepo.save(address);

        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepo.findAll();

        List<AddressDTO> addressDTOs = addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());

        return addressDTOs;
    }

    @Override
    public AddressDTO getAddress(Long addressId) {
        Address address = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public AddressDTO updateAddress(Long addressId, Address address) {
        Address addressFromDB = addressRepo.findByCountryAndStateAndCityAndPincodeAndAddress1AndAddress2(
                address.getCountry(), address.getState(), address.getCity(), address.getPincode(),
                address.getAddress1(), address.getAddress2());

        if (addressFromDB == null) {
            addressFromDB = addressRepo.findById(addressId)
                    .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

            addressFromDB.setCountry(address.getCountry());
            addressFromDB.setState(address.getState());
            addressFromDB.setCity(address.getCity());
            addressFromDB.setPincode(address.getPincode());
            addressFromDB.setAddress1(address.getAddress1());
            addressFromDB.setAddress2(address.getAddress2());

            Address updatedAddress = addressRepo.save(addressFromDB);

            return modelMapper.map(updatedAddress, AddressDTO.class);
        } else {
            List<Customer> users = userRepo.findByAddress(addressId);
            final Address a = addressFromDB;

            // users.forEach(user -> user.getAddresses().add(a));

            deleteAddress(addressId);

            return modelMapper.map(addressFromDB, AddressDTO.class);
        }
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDB = addressRepo.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        List<Customer> users = userRepo.findByAddress(addressId);

        users.forEach(user -> {
            // user.getAddresses().remove(addressFromDB);

            userRepo.save(user);
        });

        addressRepo.deleteById(addressId);

        return "Address deleted succesfully with addressId: " + addressId;
    }
}
