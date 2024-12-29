package com.app.services.impl;

import com.app.TestContainerConfig;
import com.app.TestMockConfig;
import com.app.entites.Customer;
import com.app.entites.type.UserRoleEnum;
import com.app.payloads.request.AssignCategoriesRequest;
import com.app.payloads.request.MobileSignUpRequest;
import com.app.payloads.request.VendorProfileRequest;
import com.app.repositories.RepositoryManager;
import com.app.services.ServiceManager;
import com.app.services.auth.dto.UserAuthentication;
import com.app.services.auth.signup.MobileSignUpService;
import com.app.services.constants.UserRegPlatform;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

@Slf4j
@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {TestMockConfig.class, TestContainerConfig.class})
class DefaultVendorServiceTest {

  @Autowired RepositoryManager repositoryManager;
  @Autowired ModelMapper modelMapper;
  @Autowired ServiceManager serviceManager;
  @Autowired DefaultVendorService vendorService;

  @Autowired MobileSignUpService mobileSignUpService;

  @Test
  public void testDependencyInjection() {
    Assertions.assertNotNull(vendorService);
    Assertions.assertNotNull(vendorService.getUserService());
    Assertions.assertNotNull(vendorService.getModelMapper());
    Assertions.assertNotNull(vendorService.getRepoManager());
  }

  @DisplayName("Create vendor profile by Admin")
  @Transactional
  @Rollback
  @Test
  void testCreateVendorByAdmin() {
    /* MobileSignUpRequest mobileSignUpRequest = new MobileSignUpRequest();
    mobileSignUpRequest.setCountryCode("+91");
    mobileSignUpRequest.setMobile("9912149048L");
    mobileSignUpRequest.setRegPlatform(UserRegPlatform.Web);
    mobileSignUpRequest.setUserRoleEnum(UserRoleEnum.ADMIN);
     var adminUserResponse = mobileSignUpService.processSignUp(mobileSignUpRequest);*/
    var result = repositoryManager.getCustomerRepo().findAll();

    Customer adminUser = serviceManager.getUserService().fetchUserById(1001L);
    // entityManager.persist(customer);
    VendorProfileRequest profileRequest = new VendorProfileRequest();
    profileRequest.setBusinessName("Kunta's Natural Farm");
    profileRequest.setDescription("Organic Farming,Farm Stay and Many more");
    profileRequest.setBusinessType("Agriculture");
    profileRequest.setContactPerson("Jane Doe");
    profileRequest.setCommunicationEmail("swamy.k@outlook.com");
    profileRequest.setServiceAreas(
        Map.of("areas", Set.of("502108", "502103", "Mirdoddi", "Siddipet")));
    profileRequest.setContactNumber("9912149047");
    profileRequest.setOwnerName("Swamy Kunta");
    profileRequest.setBusinessAddress(
        Map.of("address1", "33 Acres Land", "city", "Mirdoddi", "zipCode", "502108"));
    profileRequest.setAssignCategories(new AssignCategoriesRequest(Set.of(100L, 101L, 10000L)));
    UserAuthentication userAuthentication =
        new UserAuthentication(
            adminUser.getId(),
            Set.of(
                new SimpleGrantedAuthority(
                    adminUser.getRoles().stream().findFirst().get().getRoleName())));
    var vendorResponse = vendorService.createVendorProfile(profileRequest, userAuthentication);
    Assertions.assertNotNull(vendorResponse.id());
    Assertions.assertNotNull(vendorResponse.message());

    var vendorEntity = repositoryManager.getVendorRepo().findById(vendorResponse.id());
    Assertions.assertTrue(vendorEntity.isPresent());
    Assertions.assertAll(
        () -> {
          Assertions.assertNotNull(vendorEntity.get().getBusinessName());
          Assertions.assertEquals("9912149047", vendorEntity.get().getContactNumber());
          Assertions.assertNotNull(vendorEntity.get().getUserId());
          Assertions.assertNotSame(vendorEntity.get().getUserId(), adminUser.getId());
          Assertions.assertEquals(
              adminUser.getId(), Long.parseLong(vendorEntity.get().getCreatedBy()));
        });
    // vendorService.assignCategories(vendorEntity.get().getId(),profileRequest.getAssignCategories());
  }

  @DisplayName("Create vendor profile by Self")
  @Transactional
  @Rollback
  @Test
  void testCreateVendorWithVendorRole() {
    MobileSignUpRequest mobileSignUpRequest = new MobileSignUpRequest();
    mobileSignUpRequest.setCountryCode("+91");
    mobileSignUpRequest.setMobile("7876543299L");
    mobileSignUpRequest.setRegPlatform(UserRegPlatform.Android);
    mobileSignUpRequest.setUserRoleEnum(UserRoleEnum.VENDOR);

    var response = mobileSignUpService.processSignUp(mobileSignUpRequest);
    // entityManager.persist(customer);
    log.debug("Vendor user is created and id is {}", response.userId());
    VendorProfileRequest profileRequest = new VendorProfileRequest();
    //  Vendor profileRequest=new Vendor();
    profileRequest.setBusinessName("Kunta's Natural Farm");
    profileRequest.setDescription("Organic Farming,Farm Stay and Many more");
    profileRequest.setBusinessType("Agriculture");
    profileRequest.setContactPerson("Jane Doe");
    profileRequest.setCommunicationEmail("swamy.k@outlook.com");
    profileRequest.setServiceAreas(
        Map.of("areas", Set.of("502108", "502103", "Mirdoddi", "Siddipet")));
    profileRequest.setContactNumber("9912149048");
    profileRequest.setOwnerName("Swamy Kunta");
    profileRequest.setBusinessAddress(
        Map.of("address1", "33 Acres Land", "city", "Mirdoddi", "zipCode", "502108"));

    UserAuthentication userAuthentication =
        new UserAuthentication(
            response.userId(), Set.of(new SimpleGrantedAuthority(UserRoleEnum.VENDOR.name())));
    var response1 = vendorService.createVendorProfile(profileRequest, userAuthentication);
    Assertions.assertNotNull(response1.id());
    Assertions.assertNotNull(response1.message());

    var vendorEntity = repositoryManager.getVendorRepo().findById(response1.id());
    Assertions.assertTrue(vendorEntity.isPresent());
    // Assertions.assertNotNull(vendorEntity.get().getCreatedDate());
  }

  @Test
  void testFetchVendorByMobile() {
    String mobile = "919000067890";
    UserAuthentication userAuthentication =
        new UserAuthentication(
            1000L, Set.of(new SimpleGrantedAuthority(UserRoleEnum.ADMIN.name())));
    var response = vendorService.fetchVendorByMobile(mobile, userAuthentication);
    Assertions.assertNotNull(response);
  }
  /*
  void testFetchActiveVendors() {
      var vendorsList= serviceManager.getVendorService().fetchVendorsByStatus(VendorStatus.ACTIVE);
      assertAll(()->{
          assertFalse(vendorsList.isEmpty());
          assertEquals(1,vendorsList.size());
      });
  }


  void fetchVendorById() {
      var vendorResponse= serviceManager.getVendorService().fetchVendorById(vendorId);
      assertAll(()->{
          assertNotNull(vendorResponse);
        //  assertEquals(vendorId,vendorResponse.getId());
      });
  }

  @DisplayName("Get Vendors By Service Area")
  void fetchVendorsByServiceArea() {
     var vendorDetails= serviceManager.getVendorService().fetchVendorsByServiceArea(serviceArea);
     assertEquals(1,vendorDetails.size(),"No of vendors should be 1 for service area :"+serviceArea);
      vendorDetails= serviceManager.getVendorService().fetchVendorsByServiceArea("900007");
      assertEquals(0,vendorDetails.size(),"No of vendors should be 0 for zipcode: '900007'");
  }*/
}
