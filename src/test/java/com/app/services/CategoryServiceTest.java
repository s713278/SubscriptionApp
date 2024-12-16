package com.app.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.app.CommonConfig;

@SpringBootTest
@Testcontainers
@ContextConfiguration(classes = {CommonConfig.class})
class CategoryServiceTest {

    @Autowired  CategoryService categoryService;
    @Test
    void getCategoryProductMapping() {
        var data= categoryService.getCategoryProductMapping();
        assertEquals(4,data.size());
    }
}