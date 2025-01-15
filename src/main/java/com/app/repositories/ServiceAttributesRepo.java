package com.app.repositories;

import com.app.entites.ServiceAttribute;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface ServiceAttributesRepo extends CrudRepository<ServiceAttribute, Long> {

  Optional<ServiceAttribute> findBySkuId(Long skuId);
}
