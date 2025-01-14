package com.app.config;

import com.app.repositories.RepositoryManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = {TestContainerConfig.class, TestMockConfig.class})
public abstract class AbstractBaseTest {

    @Autowired
    protected RepositoryManager repositoryManager;
}
