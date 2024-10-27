package com.app.repositories;


import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("dev")
class VendorSkuPriceRepoTest {

    @MockBean
    JavaMailSender javaMailSender;
    private @Autowired VendorSkuPriceRepo vendorSkuPriceRepo;
    @Test
    public void findByVendorIdAndSkuId() {
    }

    @Test
    public void findSkusByVendorIdGroupedByProductId() {
     // var result=  vendorSkuPriceRepo.findVendorProducts(3L);
        var result= vendorSkuPriceRepo.findVendorProducts(3L);
        assertFalse( result.isEmpty());
    }
}