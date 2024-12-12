package com.app;

import java.time.ZoneId;
import java.util.TimeZone;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import com.app.repositories.RoleRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
public class SubscriptionApp implements CommandLineRunner {

    @Autowired
    private RoleRepo roleRepo;

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Kolkata")));
        SpringApplication.run(SubscriptionApp.class, args);
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
