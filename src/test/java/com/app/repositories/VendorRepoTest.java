package com.app.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import com.app.TestContainerConfig;
import com.app.entites.Customer;
import com.app.entites.Vendor;
import com.app.entites.type.ApprovalStatus;
import com.app.entites.type.VendorStatus;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {TestContainerConfig.class}) // Include MockTestConfig
public class VendorRepoTest {

  @Autowired RepositoryManager repositoryManager;

  @Test
  @Transactional
  @Rollback
  void testCreateVendor() {
    // Persist a Customer
    Customer customer = new Customer();
    customer.setCountryCode("+91");
    customer.setMobile(9876543210L);
    // entityManager.persist(customer);
    customer = repositoryManager.getCustomerRepo().save(customer);

    // Persist a Vendor
    Vendor vendor = new Vendor();
    vendor.setBusinessName("Sample Business");
    vendor.setBusinessType("Retail");
    vendor.setOwnerName("John Doe");
    vendor.setContactPerson("Jane Doe");
    vendor.setContactNumber("1234567890");
    vendor.setCommunicationEmail("swamyk@outlook.com");
    vendor.setBusinessAddress(
        Map.of("address1", "33 Acres Land", "city", "Mirdoddi", "zipCode", "502108"));
    vendor.setServiceAreas(Map.of("areas", List.of("502108", "502103", "Mirdoddi", "Siddipet")));
    vendor.setUserId(customer.getId());
    vendor.setStatus(VendorStatus.ACTIVE);
    vendor.setApprovalStatus(ApprovalStatus.PENDING);
    // entityManager.persist(vendor);
    vendor = repositoryManager.getVendorRepo().save(vendor);
    // Flush and Verify
    repositoryManager.getVendorRepo().flush();
    assertThat(customer.getId()).isNotNull();
    assertThat(vendor.getId()).isNotNull();
    assertThat(vendor.getBusinessName()).isEqualTo("Sample Business");
    Assertions.assertSame(customer.getId(), vendor.getUserId());
    Assertions.assertEquals(VendorStatus.ACTIVE, vendor.getStatus());
    Assertions.assertEquals(ApprovalStatus.PENDING, vendor.getApprovalStatus());

    // Updating the status
    repositoryManager.getVendorRepo().updateVendorStatus(vendor.getId(), VendorStatus.INACTIVE);
    repositoryManager.getVendorRepo().flush();
    var optionalVendor = repositoryManager.getVendorRepo().findById(vendor.getId());
    Assertions.assertTrue(optionalVendor.isPresent());
    Assertions.assertSame(VendorStatus.INACTIVE, optionalVendor.get().getStatus());

    // Updating the vendor status and approval status
    repositoryManager
        .getVendorRepo()
        .updateApprovalStatus(vendor.getId(), ApprovalStatus.REJECTED, VendorStatus.INACTIVE);
    repositoryManager.getVendorRepo().flush();
    optionalVendor = repositoryManager.getVendorRepo().findById(vendor.getId());
    Assertions.assertTrue(optionalVendor.isPresent());
    Assertions.assertSame(VendorStatus.INACTIVE, optionalVendor.get().getStatus());
    Assertions.assertSame(ApprovalStatus.REJECTED, optionalVendor.get().getApprovalStatus());
  }
}
