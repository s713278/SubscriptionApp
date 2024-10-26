package com.app.services.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.app.entites.Vendor;
import com.app.entites.type.VendorStatus;
import com.app.entites.type.VerificationStatus;
import com.app.exceptions.APIException;
import com.app.payloads.VendorDTO;

@Transactional
@SpringBootTest
@ActiveProfiles("dev")
class VendorServiceTest {

    @MockBean
    private JavaMailSender javaMailSender; // Mock the JavaMailSender
    @Autowired
    private VendorService vendorService;
    @Autowired
    private ModelMapper modelMapper;
    private Long vendorId;
    private String serviceArea="502108";
    @BeforeEach
    void setUp() {
        Vendor vendor=new Vendor();
        vendor.setBusinessName("Kunta's Natural Farm");
        vendor.setEmail("knf@example.com");
        vendor.setServiceAreas(Map.of("areas", List.of("502108","502103","Mirdoddi","Siddipet")));
        vendor.setStatus(VendorStatus.ACTIVE);
        vendor.setContactNumber("9912149048");
        vendor.setOwnerName("Swamy Kunta");
        vendor.setVerificationStatus(VerificationStatus.PENDING);
        vendor.setBusinessAddress(Map.of("address1","33 Acres Land",
                "city","Mirdoddi","zipCode","502108"));
        var vendorResponse=vendorService.createVendor(modelMapper.map(vendor, VendorDTO.class));
        vendorId=vendorResponse.getData().getId();
        assertNotNull(vendorId);
      //  assertEquals(VendorStatus.ACTIVE,vendorResponse.);
    }

    @AfterEach
    void tearDown() {
        vendorService.deleteVendor(vendorId);
    }

    @Test
    void testFetchActiveVendors() {
        var vendorsList= vendorService.fetchVendorsByStatus(VendorStatus.ACTIVE.name());
        assertAll(()->{
            assertFalse(vendorsList.isEmpty());
            assertEquals(1,vendorsList.size());
        });
    }
    @Test
    void testFetchInActiveVendors() {
        var vendorsList= vendorService.fetchVendorsByStatus(VendorStatus.SUSPENDED.name());
        assertTrue(vendorsList.isEmpty());
    }
    @Test
    void testFetchInValidStatusQuery() {
         assertThrows(APIException.class,()->vendorService.fetchVendorsByStatus("JunkStatus"));

    }
    @Test
    void fetchVendorById() {
        var vendorResponse= vendorService.fetchVendorById(vendorId);

        assertAll(()->{
            assertNotNull(vendorResponse);
            assertEquals(vendorId,vendorResponse.getId());
        });
    }

    @DisplayName("Get Vendors By Service Area")
    @Test
    void fetchVendorsByServiceArea() {
       var vendorDetails= vendorService.fetchVendorsByServiceArea(serviceArea);
       assertEquals(1,vendorDetails.size(),"No of vendors should be 1 for service area :"+serviceArea);

        vendorDetails= vendorService.fetchVendorsByServiceArea("900007");
        assertEquals(0,vendorDetails.size(),"No of vendors should be 0 for zipcode: '900007'");
    }
}