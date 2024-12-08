package com.app.services;

import com.app.AbstractBaseConfig;
import com.app.entites.Customer;
import com.app.entites.type.SubFrequency;
import com.app.payloads.request.CreateSubscriptionRequest;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.repositories.RepositoryManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class SubscriptionServiceTest extends AbstractBaseConfig {
    
    @Autowired
    private  SubscriptionService subscriptionService;
    
    @Autowired
    private  AbstractCreateSubscriptionService createSubscriptionService;
    
    @Autowired
    private  RepositoryManager reposirotyManager;

    private Customer testCustomer;

    private final String email = "swamy.ramya@example.com";
    private Long customerId = null;
    private final Long testVendorId = 1L;
    private final Long testSkuId = 1L;
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
        CreateSubscriptionRequest createSubscriptionRequest = new CreateSubscriptionRequest();
        createSubscriptionRequest.setSkuId(testVendorId);
        createSubscriptionRequest.setQuantity(5);
        createSubscriptionRequest.setStartDate(LocalDate.now().plusDays(2));
        createSubscriptionRequest.setFrequency(SubFrequency.DAILY);
        createSubscriptionRequest.setEndDate(LocalDate.now().plusDays(30));
        var sub = createSubscriptionService.createSubscription(customerId, createSubscriptionRequest);
        assertNotNull( sub);
        assertNotNull(sub.subscriptionId());
        testSubscriptionId = sub.subscriptionId();
    }

    
    @Test()
    void testUpdateSubscription() {
        var oldSub=subscriptionService.fetchSubscription(testSubscriptionId);
        UpdateSubscriptionRequest request = new UpdateSubscriptionRequest();
        request.setQuantity(9);
        request.setStartDate(LocalDate.now().plusDays(10));
        request.setFrequency(SubFrequency.ALTERNATE_DAY);
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
