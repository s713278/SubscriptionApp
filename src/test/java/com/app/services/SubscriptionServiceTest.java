package com.app.services;

import static org.junit.jupiter.api.Assertions.*;

import com.app.TestContainerConfig;
import com.app.TestMockConfig;
import com.app.entites.Customer;
import com.app.payloads.request.CreateSubscriptionRequest;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.repositories.RepositoryManager;
import java.time.LocalDate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestContainerConfig.class, TestMockConfig.class})
class SubscriptionServiceTest {

  @Autowired private SubscriptionQueryService subscriptionService;

  @Autowired private AbstractSubscriptionCreateService createSubscriptionService;

  @Autowired private RepositoryManager repositoryManager;

  private Customer testCustomer;

  private final String email = "swamy.ramya@example.com";
  private Long customerId = 1000L;
  private final Long testVendorId = 1L;
  private final Long testSkuId = 202L;
  private Long testSubscriptionId = null;
  private final Long priceId = 8L;

  // private final Long vendorId = 91L;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {
    // Clean up the customer repository after each test
    // repositoryManager.getCustomerRepo().deleteById(customerId);
    System.out.println("It should be only one time --tearDown");
  }

  @DisplayName("Create Subscription with Frequency:ONE_TIME and Delivery Mode: FIXED ")
  @Test
  void testCreateSubscriptionForOneTimeFrequency() {
    CreateSubscriptionRequest request = new CreateSubscriptionRequest();
    request.setSkuId(testSkuId);
    request.setQuantity(5);
    request.setPriceId(priceId);
    request.setSkuSubscriptionPlanId(9L);
    request.setDeliveryDate(LocalDate.now().plusDays(3));
    //  request.setFrequency(SubFrequency.ONE_TIME);
    // request.setEndDate(LocalDate.now().plusDays(30));
    var sub = createSubscriptionService.createSubscription(customerId, request);
    assertNotNull(sub);
    assertNotNull(sub.subscriptionId());
    testSubscriptionId = sub.subscriptionId();
    testUpdateSubscription(testSubscriptionId);
  }

  // @Test()
  void testUpdateSubscription(Long subId) {
    var oldSub = subscriptionService.fetchSubscription(testSubscriptionId);
    UpdateSubscriptionRequest request = new UpdateSubscriptionRequest();
    request.setQuantity(9);
    request.setStartDate(LocalDate.now().plusDays(10));
    request.setSkuSubscriptionPlanId(testSubscriptionId);
    // request.setFrequency(SubFrequency.ALTERNATE_DAY);
    var response = subscriptionService.updateSubscription(testSubscriptionId, customerId, request);
    assertTrue(response.success());
    var updateSub = subscriptionService.fetchSubscription(testSubscriptionId);
    assertNotEquals(oldSub.getQuantity(), updateSub.getQuantity(), "Quantity changes looks good!!");
    assertNotEquals(
        oldSub.getStartDate(), updateSub.getStartDate(), "Start date changes looks good!!");
    assertNotEquals(
        oldSub.getNextDeliveryDate(),
        updateSub.getNextDeliveryDate(),
        "Next delivery changes looks good!!");
    // assertNotEquals(
    //   oldSub.getFrequency(), updateSub.getFrequency(), "Frequency changes looks good!!");
  }

  @Test
  void testFetchUserSubscriptionsForSpecificVendor() {
    var subs = subscriptionService.fetchSubsByUserAndVendor(customerId, testVendorId);
    assertFalse(subs.isEmpty());
  }
}
