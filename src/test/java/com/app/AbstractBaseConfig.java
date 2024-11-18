package com.app;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("dev")
@Import(MockTestConfig.class)
public abstract  class AbstractBaseConfig {


}
