package com.app.repositories;

import static org.junit.jupiter.api.Assertions.*;

import com.app.config.TestContainerConfig;
import com.app.config.TestMockConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestContainerConfig.class, TestMockConfig.class})
class CategoryRepoTest {

  @Autowired CategoryRepo categoryRepo;

  @Test
  void findCategoriesByServiceArea() {
    var result = categoryRepo.findCategoriesByServiceArea("502018");
    assertNotNull(result);
  }
}
