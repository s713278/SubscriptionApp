package com.app;

import com.app.repositories.RoleRepo;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@Slf4j
@SpringBootApplication
@SecurityScheme(
    name = "E-Commerce Application",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER)
public class ECommerceApplication implements CommandLineRunner {

  @Autowired private RoleRepo roleRepo;

  public static void main(String[] args) {
    SpringApplication.run(ECommerceApplication.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Override
  public void run(String... args) {
    try {
      long count = roleRepo.count();
      if (count != 3) throw new RuntimeException("Initiatial data missed!!!!");
    } catch (Exception e) {
      log.error("Error During the initial ROLES ..data setup {} ", e.getMessage(), e);
    }
  }
}
