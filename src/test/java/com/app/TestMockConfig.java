package com.app;

import static org.mockito.Mockito.mock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@TestConfiguration
public class TestMockConfig {

  @Bean
  public JavaMailSender javaMailSender() {
    return mock(JavaMailSender.class); // Create a mock JavaMailSender instance
  }
}