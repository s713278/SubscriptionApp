package com.app;

import com.app.config.AppConstants;
import com.app.repositories.RoleRepo;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import java.time.ZoneId;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication
@EnableAsync
@SecurityScheme(name = AppConstants.SECURITY_CONTEXT_PARAM, scheme = "bearer", type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class ECommerceApplication implements CommandLineRunner {

    @Autowired
    private RoleRepo roleRepo;

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Kolkata")));
        SpringApplication.run(ECommerceApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Override
    public void run(String... args) {
        try {
            roleRepo.findByRoleName("ADMIN");
            long count = roleRepo.count();
            if (count != 3) throw new RuntimeException("Initiatial data missed!!!!");
        } catch (Exception e) {
            log.error("No pre defined roles defined in database ,Please check the data base tb_roles table {} ", e.getMessage(), e);
        }
    }
}
