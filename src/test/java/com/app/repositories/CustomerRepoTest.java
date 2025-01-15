package com.app.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.app.config.TestContainerConfig;
import com.app.config.TestMockConfig;
import com.app.entites.Customer;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {TestContainerConfig.class, TestMockConfig.class})
@Slf4j
class CustomerRepoTest {

  @Autowired private RepositoryManager repositoryManager;

  private Customer testCustomer;

  private final String email = "swamy.ramya@example.com";
  private Long customerId = null;

  @BeforeEach
  void setUp() {
    // Create a test customer before each test
    testCustomer = new Customer();
    testCustomer.setFirstName("swamyk");
    testCustomer.setEmail(email);
    testCustomer.setCountryCode("+91");
    testCustomer.setMobile(9912149048L);
    customerId = repositoryManager.getCustomerRepo().save(testCustomer).getId();
  }

  @AfterEach
  void tearDown() {
    // Clean up the customer repository after each test
    repositoryManager.getCustomerRepo().deleteById(customerId);
    System.out.println("tearDown");
  }

  @Test
  void testFindById() {
    // Given a customer was saved in setUp

    // When we count the number of customers
    var optionalCustomer = repositoryManager.getCustomerRepo().findById(customerId);

    // Then we expect the count to be 1
    assertTrue(optionalCustomer.isPresent());
  }

  @Test
  void testFindByEmail() {
    // When we retrieve a customer by email
    Optional<Customer> customer = repositoryManager.getCustomerRepo().findByEmail(email);

    // Then we expect the customer to be present and match the saved customer
    assertTrue(customer.isPresent());
    assertEquals(testCustomer.getEmail(), customer.get().getEmail());
    assertEquals(testCustomer.getFirstName(), customer.get().getFirstName());
  }
}
