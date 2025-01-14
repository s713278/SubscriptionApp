package com.app.repositories;

import com.app.config.AbstractBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
class VendorProductRepoTest extends AbstractBaseTest {

    @Test
    void getCount() {
        var result = repositoryManager.getVendorProductRepo().findByVendorId(91L);
        assertEquals(7,result.size());
    }

    @Test
    void deleteProductsByVendorId() {
        var count = repositoryManager.getVendorProductRepo().deleteProductsByVendorId(91L, Set.of(5L,6L,7L,8L,9L));
        assertEquals(5,count);
        var result = repositoryManager.getVendorProductRepo().findByVendorId(91L);
        assertEquals(2,result.size());
    }
}