package com.app.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.AbstractBaseConfig;
import com.app.entites.Customer;
import com.app.entites.SubscriptionFrequency;
import com.app.payloads.request.SubscriptionRequest;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.repositories.RepositoryManager;

import jakarta.transaction.Transactional;

@Transactional
class SubscriptionServiceTest extends AbstractBaseConfig {
    
    @Autowired
    private  SubscriptionService subscriptionService;
    
    @Autowired
    private  AbstractCreateSubscriptionService createSubscriptionService;
    
    @Autowired
    private  RepositoryManager reposirotyManager;

    private Customer testCustomer;

    private String email = "swamy.ramya@example.com";
    private Long customerId = null;
    private Long testVendorId = 1L;
    private Long testSkuId = 1L;
    private Long testSubscriptionId=null;
    
    @BeforeEach
    void setUp() {
        // Create a test customer before each test
        testCustomer = new Customer();
        testCustomer.setFirstName("swamyk");
        testCustomer.setEmail(email);
        testCustomer.setMobile(9912149048L);
        customerId = reposirotyManager.getCustomerRepo().save(testCustomer).getId();
        System.out.println("It should be only one time --setUp");
       testCreateSubscription();
    }

    @AfterEach
    void tearDown() {
        // Clean up the customer repository after each test
        reposirotyManager.getCustomerRepo().deleteById(customerId);
        System.out.println("It should be only one time --tearDown");
    }

    @Test
    void testCreateSubscription() {
        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
        subscriptionRequest.setVendorPriceId(testVendorId);
        subscriptionRequest.setQuantity(5);
        subscriptionRequest.setStartDate(LocalDate.now().plusDays(2));
        subscriptionRequest.setFrequency(SubscriptionFrequency.DAILY);
        subscriptionRequest.setEndDate(LocalDate.now().plusDays(30));
        var sub = createSubscriptionService.createSubscription(customerId,testVendorId,subscriptionRequest);
        assertNotNull( sub.data());
        assertTrue(sub.success());
        testSubscriptionId = sub.data().id();
    }

    
    @Test()
    void testUpdateSubscription() {
        var oldSub=subscriptionService.fetchSubscription(testSubscriptionId);
        UpdateSubscriptionRequest request = new UpdateSubscriptionRequest();
        request.setQuantity(9);
        request.setStartDate(LocalDate.now().plusDays(10));
        request.setFrequency(SubscriptionFrequency.ALTERNATE_DAY);
         var response = subscriptionService.updateSubscription(testSubscriptionId,customerId, request);
         assertTrue(response.success());
         var updateSub=subscriptionService.fetchSubscription(testSubscriptionId);
         assertNotEquals(oldSub.getQuantity(), updateSub.getQuantity(),"Quantity changes looks good!!");
         assertNotEquals(oldSub.getStartDate(), updateSub.getStartDate(),"Startdate changes looks good!!");
         assertNotEquals(oldSub.getNextDeliveryDate(), updateSub.getNextDeliveryDate(),"Next delivery changes looks good!!");
         assertNotEquals(oldSub.getFrequency(), updateSub.getFrequency(),"Frequency changes looks good!!");
    }


   @Test
    void testFetchUserSubscriptionsForSpecificVendor(){
      var subs= subscriptionService.fetchSubsByUserAndVendor(customerId,testVendorId);
      assertFalse(subs.isEmpty());
   }

}
