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
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.app.CommonConfig;
import com.app.entites.Vendor;
import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.VendorStatus;
import com.app.payloads.VendorDetailsDTO;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {CommonConfig.class})
class VendorServiceTest {

    @MockBean
    private JavaMailSender javaMailSender; // Mock the JavaMailSender
    @Autowired
    private VendorService vendorService;
    @Autowired
    private ModelMapper modelMapper;
    private Long vendorId;
    private final String serviceArea="502108";
    @BeforeEach
    void setUp() {
        Vendor vendor=new Vendor();
        vendor.setBusinessName("Kunta's Natural Farm");
        vendor.setDescription("Organic Farming,Farm Stay and Many more");
        vendor.setBusinessType("Agriculture");
        vendor.setCommunicationEmail("knf@example.com");
        vendor.setServiceAreas(Map.of("areas", List.of("502108","502103","Mirdoddi","Siddipet")));
        vendor.setStatus(VendorStatus.ACTIVE);
        vendor.setContactNumber("9912149048");
        vendor.setOwnerName("Swamy Kunta");
        vendor.setApprovalStatus(ApprovalStatus.PENDING);
        vendor.setBusinessAddress(Map.of("address1","33 Acres Land",
                "city","Mirdoddi","zipCode","502108"));
        var response=vendorService.createVendor(modelMapper.map(vendor, VendorDetailsDTO.class));
        assertNotNull(response.id());
    }

    @AfterEach
    void tearDown() {
        vendorService.deleteVendor(vendorId);
    }

    @Test
    void testFetchActiveVendors() {
        var vendorsList= vendorService.fetchVendorsByStatus(VendorStatus.ACTIVE);
        assertAll(()->{
            assertFalse(vendorsList.isEmpty());
            assertEquals(1,vendorsList.size());
        });
    }

    @Test
    void fetchVendorById() {
        var vendorResponse= vendorService.fetchVendorById(vendorId);
        assertAll(()->{
            assertNotNull(vendorResponse);
          //  assertEquals(vendorId,vendorResponse.getId());
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