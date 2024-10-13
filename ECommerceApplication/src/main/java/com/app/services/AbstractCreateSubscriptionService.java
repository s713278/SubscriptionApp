package com.app.services;

import com.app.entites.Customer;
import com.app.entites.Sku;
import com.app.entites.Subscription;
import com.app.entites.SubscriptionFrequency;
import com.app.entites.SubscriptionStatus;
import com.app.entites.Vendor;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.SubscriptionRequest;
import com.app.payloads.response.SubscriptionResponse;
import com.app.repositories.RepositoryManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCreateSubscriptionService {

    private final RepositoryManager repoManager;
    private final SubscriptionServiceHelper serviceHelper;

    protected void preSubscription(SubscriptionRequest request) {
        
    }

    protected void postSubscription(Subscription subscription) {
           // Notify the customer after subscription creation
        notifyCustomer(subscription);
    }
    @Transactional
    public SubscriptionResponse createSubscription(SubscriptionRequest request) {
        log.info("Start - Create subscription request for customer {}",request.getCustomerId());
        Subscription subscription = new Subscription();
        try {
            preSubscription(request);
            // Fetch customer and tenant info
            Customer customer = repoManager.getCustomerRepo().findById(request.getCustomerId())
                    .orElseThrow(() ->new APIException(APIErrorCode.API_400, "Customer not found"));

            // Set SKU, quantity, and frequency
            Sku sku = repoManager.getSkuRepo().findById(request.getSkuId())
                    .orElseThrow(() -> new APIException(APIErrorCode.API_400,"SKU not found"));

            Vendor vendor =repoManager.getVendorRepo().findById(request.getVendorId()).orElseThrow(() -> new APIException(APIErrorCode.API_400, "Vendor not found!!"));
            subscription.setCustomer(customer);
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setSku(sku);
            subscription.setQuantity(request.getQuantity());
            subscription.setFrequency(request.getFrequency());
            subscription.setVendor(vendor);

            if (request.getFrequency() == SubscriptionFrequency.CUSTOM) {
                subscription.setCustomDays(request.getCustomDays());
            }

            subscription.setFromStartDate(request.getFromStartDate());
            subscription.setNextDeliveryDate(serviceHelper.calculateNextDeliveryDate(subscription));
            subscription.setUpdateVersion(1); //Created First Time
            subscription = repoManager.getSubscriptionRepo().save(subscription);
            postSubscription(subscription);
        } catch (Exception e) {
            log.error("Unable to create subscription for customer {}", request.getCustomerId(), e);
            new APIException(APIErrorCode.API_418, e.getMessage());
        }
        // Create the Data object with subscription_id and next_delivery_date
        SubscriptionResponse.Data data = new SubscriptionResponse.Data(subscription.getId(),
                subscription.getNextDeliveryDate());
        log.info("End - Create subscription request for customer {}",request.getCustomerId());
        return new SubscriptionResponse(true, "Subscription created successfully", data);
    }


    protected abstract void notifyCustomer(Subscription subscription);
}
