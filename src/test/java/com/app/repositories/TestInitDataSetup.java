package com.app.repositories;

import com.app.TestContainerConfig;
import com.app.TestMockConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestContainerConfig.class, TestMockConfig.class})
public class TestInitDataSetup {

  @Autowired private RepositoryManager repositoryManager;

  @Test
  void testInitDataSetup() {
    long categoryCount = repositoryManager.getCategoryRepo().count();
    Assertions.assertEquals(13, categoryCount);
    long productsCount = repositoryManager.getProductRepo().count();
    Assertions.assertEquals(9, productsCount);
    long skusCount = repositoryManager.getSkuRepo().count();
    Assertions.assertEquals(5, skusCount);
  }
}
