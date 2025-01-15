package com.app.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.app.config.TestContainerConfig;
import com.app.config.TestMockConfig;
import com.app.entites.Customer;
import com.app.entites.type.DeliveryMode;
import com.app.entites.type.SkuType;
import com.app.entites.type.SubFrequency;
import com.app.payloads.request.CreateSubscriptionRequest;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.payloads.response.ItemSubscriptionResponse;
import com.app.payloads.response.ServiceSubscriptionResponse;
import com.app.payloads.response.SubscriptionCreateResponse;
import com.app.repositories.RepositoryManager;
import com.app.services.constants.PaymentType;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger log = LoggerFactory.getLogger(SubscriptionCreateServiceTest.class);
  @Autowired private SubscriptionQueryService subscriptionService;

  @Autowired private AbstractSubscriptionCreateService createSubscriptionService;

  @Autowired private RepositoryManager repositoryManager;

  private Customer testCustomer;

  private final String email = "swamy.ramya@example.com";
  private Long customerId = 1000L;
  private final Long testVendorId = 1L;
  private final Long testOneTimeSubscriptionFixedId = 202L;

  private final Long priceId = 8L;
  private final Long testServiceSkuId = 502L;
  private final Long serviceSkuPriceId = 16L;

  // private final Long vendorId = 91L;

  // Test data for ONE_TIME with Flexible delivery mode
  private final Long testOneTimeFlexiSkuId = 652L;
  private final Long testOneTimeFlexiPriceId = 17L;
  private final Long testOneTimeFlexiSubPlanId = 20L;

  private final Long testSubscriptionId = 100L;

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {
    // Clean up the customer repository after each test
    // repositoryManager.getCustomerRepo().deleteById(customerId);
    System.out.println("It should be only one time --tearDown");
  }

  @DisplayName("Create ITEM sub with Frequency:ONE_TIME and Delivery Mode: FIXED ")
  @Test
  void testCreateItemSKU() {
    CreateSubscriptionRequest request = new CreateSubscriptionRequest();
    request.setSkuId(testOneTimeSubscriptionFixedId);
    request.setQuantity(5);
    request.setPriceId(priceId);
    request.setSkuSubscriptionPlanId(9L);

    LocalDate today = LocalDate.now(); // Or any LocalDate you have
    LocalDate nextFriday = today.with(TemporalAdjusters.next(DayOfWeek.FRIDAY));
    request.setDeliveryDate(nextFriday);
    //  request.setFrequency(SubFrequency.ONE_TIME);
    // request.setEndDate(LocalDate.now().plusDays(30));
    var response = createSubscriptionService.createSubscription(customerId, request);
    assertResponse(response);
    assertSame(DeliveryMode.FIXED, response.getDeliveryMode());
  }

  @DisplayName("Create ITEM sub with Frequency:ONE_TIME and Delivery Mode: Flexible ")
  @Test
  void testCreateItemSKUWithFlexibleDay() {
    CreateSubscriptionRequest request = new CreateSubscriptionRequest();
    request.setSkuId(testOneTimeFlexiSkuId);
    request.setQuantity(2);
    request.setPriceId(testOneTimeFlexiPriceId);
    request.setSkuSubscriptionPlanId(testOneTimeFlexiSubPlanId);

    LocalDate today = LocalDate.now(); // Or any LocalDate you have
    LocalDate nextSunday = today.with(TemporalAdjusters.next(DayOfWeek.SUNDAY));
    request.setDeliveryDate(nextSunday);
    //  request.setFrequency(SubFrequency.ONE_TIME);
    // request.setEndDate(LocalDate.now().plusDays(30));
    var response = createSubscriptionService.createSubscription(customerId, request);
    assertResponse(response);
    assertSame(DeliveryMode.FLEXIBLE, response.getDeliveryMode());
    ;
    // testUpdateSubscription(testSubscriptionId);
  }

  private static void assertResponse(SubscriptionCreateResponse response) {
    assertNotNull(response);
    assertInstanceOf(ItemSubscriptionResponse.class, response);
    assertAll(
        () -> {
          ItemSubscriptionResponse itemResponse = (ItemSubscriptionResponse) response;
          assertNotNull(response.getSubscriptionId());
          assertSame(SkuType.ITEM, response.getSkuType());
          assertSame(SubFrequency.ONE_TIME, response.getFrequency());
          assertSame(PaymentType.CASH_ON_DELIVERY.name(), response.getPaymentMethod());
          assertNotNull(response.getVendorName());
          assertNotNull(response.getCreateDate());
          assertNotNull(response.getMobileNumber());
          assertNotNull(response.getStatus());
          assertNotNull(itemResponse.getQuantity());
          assertNotNull(itemResponse.getDeliveryAddress());
          assertNotNull(itemResponse.getNextDeliveryDate());
        });
  }

  @DisplayName(
      "Create Service SKU Subscription with Frequency:ONE_TIME and Delivery Mode: FLEXIBLE ")
  @Test
  void testCreateServiceSKU() {
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
    log.info("Response {}", response);
    assertInstanceOf(ServiceSubscriptionResponse.class, response);
    assertAll(
        () -> {
          ServiceSubscriptionResponse serviceResponse = (ServiceSubscriptionResponse) response;
          assertNotNull(response.getSubscriptionId());
          assertSame(SkuType.SERVICE, response.getSkuType());
          assertSame(SubFrequency.ONE_TIME, response.getFrequency());
          assertSame(DeliveryMode.FLEXIBLE, response.getDeliveryMode());
          assertNotNull(response.getVendorName());
          assertNotNull(response.getCreateDate());
          assertNotNull(response.getMobileNumber());
          assertNotNull(response.getStatus());
          assertNotNull(serviceResponse.getStartDate());
          assertNotNull(serviceResponse.getServiceValidity());
          assertNotNull(serviceResponse.getNoOfUses());
          assertNotNull(serviceResponse.getExpirationDate());
          assertNotNull(serviceResponse.getServiceLocation());
        });
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
