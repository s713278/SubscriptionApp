package com.app.repositories;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.app.CommonConfig;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {CommonConfig.class})
public class TestInitDataSetup {

    @Autowired
    private RepositoryManager repositoryManager;

    @Test
    void testInitDataSetup(){
        long categoryCount =repositoryManager.getCategoryRepo().count();
        Assertions.assertEquals(4, categoryCount);
        long productsCount =repositoryManager.getProductRepo().count();
        Assertions.assertEquals(4, productsCount);
        long skusCount =repositoryManager.getSkuRepo().count();
        Assertions.assertEquals(4, skusCount);
    }
}
