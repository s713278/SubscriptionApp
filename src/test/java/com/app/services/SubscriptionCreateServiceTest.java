package com.app.services;

import static org.junit.jupiter.api.Assertions.*;

import com.app.TestContainerConfig;
import com.app.TestMockConfig;
import com.app.entites.Customer;
import com.app.payloads.request.CreateSubscriptionRequest;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.repositories.RepositoryManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
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
class SubscriptionCreateServiceTest {

  @Autowired private SubscriptionQueryService subscriptionService;

  @Autowired private AbstractSubscriptionCreateService createSubscriptionService;

  @Autowired private RepositoryManager repositoryManager;

  private Customer testCustomer;

  private final String email = "swamy.ramya@example.com";
  private Long customerId = 1000L;
  private final Long testVendorId = 1L;
  private final Long testItemSkuId = 202L;

  private Long testSubscriptionId = null;
  private final Long priceId = 8L;

  private final Long testServiceSkuId = 502L;
  private final Long serviceSkuPriceId = 16L;

  // private final Long vendorId = 91L;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {
    // Clean up the customer repository after each test
    // repositoryManager.getCustomerRepo().deleteById(customerId);
    System.out.println("It should be only one time --tearDown");
  }

  @DisplayName("Create ITEM SKU Subscription with Frequency:ONE_TIME and Delivery Mode: FIXED ")
  @Test
  void testCreateItemSKUSubscriptionForOneTime() {
    CreateSubscriptionRequest request = new CreateSubscriptionRequest();
    request.setSkuId(testItemSkuId);
    request.setQuantity(5);
    request.setPriceId(priceId);
    request.setSkuSubscriptionPlanId(9L);

    LocalDate today = LocalDate.now(); // Or any LocalDate you have
    LocalDate nextSunday = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
    request.setDeliveryDate(nextSunday);
    //  request.setFrequency(SubFrequency.ONE_TIME);
    // request.setEndDate(LocalDate.now().plusDays(30));
    var sub = createSubscriptionService.createSubscription(customerId, request);
    assertNotNull(sub);
    assertNotNull(sub.getSubscriptionId());
    testSubscriptionId = sub.getSubscriptionId();
    testUpdateSubscription(testSubscriptionId);
  }

  @DisplayName(
      "Create Service SKU Subscription with Frequency:ONE_TIME and Delivery Mode: FLEXIBLE ")
  @Test
  void testCreateServiceSKUSubscription() {
    CreateSubscriptionRequest request = new CreateSubscriptionRequest();
    request.setSkuId(testServiceSkuId);
    request.setQuantity(1);
    request.setPriceId(serviceSkuPriceId);
    request.setSkuSubscriptionPlanId(17L);

    // LocalDate today = LocalDate.now(); // Or any LocalDate you have
    // LocalDate nextSunday = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
    // request.setDeliveryDate(nextSunday);
    //  request.setFrequency(SubFrequency.ONE_TIME);
    // request.setEndDate(LocalDate.now().plusDays(30));
    var response = createSubscriptionService.createSubscription(customerId, request);
    assertNotNull(response);
    assertNotNull(response.getSubscriptionId());
    System.out.println("Response \t:" + response);
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

  // @Test
  void testFetchUserSubscriptionsForSpecificVendor() {
    var subs = subscriptionService.fetchSubsByUserAndVendor(customerId, testVendorId);
    assertFalse(subs.isEmpty());
  }
}
