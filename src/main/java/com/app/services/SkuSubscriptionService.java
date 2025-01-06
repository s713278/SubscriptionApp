package com.app.services;

import com.app.constants.CacheType;
import com.app.entites.SkuSubscriptionPlan;
import com.app.entites.type.SubFrequency;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.repositories.RepositoryManager;
import com.app.repositories.projections.SKUSubscriptionPlanProjection;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SkuSubscriptionService {

  private final RepositoryManager repositoryManager;

  // Create or Update a SkuSubscription
  public SkuSubscriptionPlan saveSkuSubscription(SkuSubscriptionPlan skuSubscriptionPlan) {
    return repositoryManager.getSkuSubscriptionRepo().save(skuSubscriptionPlan);
  }

  // Get all SkuSubscriptions
  public List<SkuSubscriptionPlan> getAllSkuSubscriptions() {
    return repositoryManager.getSkuSubscriptionRepo().findAll();
  }

  // Get a SkuSubscription by ID
  public Optional<SkuSubscriptionPlan> getSkuSubscriptionById(Long id) {
    return repositoryManager.getSkuSubscriptionRepo().findById(id);
  }

  // Delete a SkuSubscription
  public void deleteSkuSubscription(Long id) {
    repositoryManager.getSkuSubscriptionRepo().deleteById(id);
  }

  // Custom business logic can be added here if needed
  @Cacheable(value = CacheType.CACHE_TYPE_VENDORS, key = "#skuId + '_' + #frequency")
  public SkuSubscriptionPlan fetchBySkuIdAndFrequency(Long skuId, SubFrequency frequency) {
    return repositoryManager
        .getSkuSubscriptionRepo()
        .findBySkuIdAndSubscriptionPlanFrequency(skuId, frequency)
        .orElseThrow(
            () ->
                new APIException(
                    APIErrorCode.BAD_REQUEST_RECEIVED,
                    "No defined subscription plans found for skuId :"
                        + skuId
                        + " with frequency: "
                        + frequency));
  }

  @Cacheable(value = CacheType.CACHE_TYPE_PRODUCTS, key = "#planId")
  public SKUSubscriptionPlanProjection fetchSkuSubscriptionPlan(Long planId) {
    return repositoryManager
        .getSkuSubscriptionRepo()
        .findSkuSubscriptionPlanById(planId)
        .orElseThrow(
            () ->
                new APIException(
                    APIErrorCode.BAD_REQUEST_RECEIVED, "Invalid subscription plan id " + planId));
  }
}
