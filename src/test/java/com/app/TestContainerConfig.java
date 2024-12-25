package com.app;


import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

//In Spring Boot 3.1.0, we can use the @ServiceConnection annotation to register the Database connection
// into the @Container, insteads of using the @DynamicPropertySource annotation.
@TestConfiguration
@SpringBootTest
public class TestContainerConfig {

    @Container
    protected static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:16-alpine")
                   .withDatabaseName("testdb")
                    .withUsername("test")
                    .withPassword("test")
                    .withInitScript("init_test_data.sql");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL_CONTAINER::getPassword);
     //   registry.add("spring.jpa.properties.hibernate.dialect",() -> "org.hibernate.dialect.PostgreSQLDialect");
      //  registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }

}
