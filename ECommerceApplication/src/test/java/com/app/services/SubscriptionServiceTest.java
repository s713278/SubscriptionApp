package com.app.services;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import com.app.entites.Customer;
import com.app.entites.SubscriptionFrequency;
import com.app.payloads.request.SubscriptionRequest;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.repositories.RepositoryManager;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
@Profile("dev")
class SubscriptionServiceTest {
    
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
      //  testCreateSubscription();   
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
        subscriptionRequest.setSkuId(testSkuId);
        subscriptionRequest.setCustomerId(customerId);
        subscriptionRequest.setVendorId(testVendorId);
        subscriptionRequest.setQuantity(5);
        subscriptionRequest.setFromStartDate(LocalDate.now().plusDays(2));
        var sub = createSubscriptionService.createSubscription(subscriptionRequest); 
        assertNotNull( sub.data());
        assertTrue(sub.success());
        testSubscriptionId = sub.data().id();
    }

    
    @Test()
    void testUpdateSubscription() {
        var oldSub=subscriptionService.fetchSubscription(testSubscriptionId);
        
        UpdateSubscriptionRequest request = new UpdateSubscriptionRequest();
        request.setSubscriptionId(testSubscriptionId);
        request.setCustomerId(customerId);
        
        request.setQuantity(9);
        request.setStartDate(LocalDate.now().plusDays(10));
        request.setFrequency(SubscriptionFrequency.ALTERNATE_DAY);
         var response = subscriptionService.updateSubscription(request); 
         assertTrue(response.success());
         var updateSub=subscriptionService.fetchSubscription(testSubscriptionId);
         assertNotEquals(oldSub.getQuantity(), updateSub.getQuantity(),"Quantity changes looks good!!");
         assertNotEquals(oldSub.getFromStartDate(), updateSub.getFromStartDate(),"Startdate changes looks good!!");
         assertNotEquals(oldSub.getNextDeliveryDate(), updateSub.getNextDeliveryDate(),"Next delivery changes looks good!!");
         assertNotEquals(oldSub.getFrequency(), updateSub.getFrequency(),"Frequence changes looks good!!");
    }

    //@Test
    void testUpdateSubscriptionStatus() {
        fail("Not yet implemented");
    }

    //@Test
    void testDeleteSubscription() {
        fail("Not yet implemented");
    }

}
