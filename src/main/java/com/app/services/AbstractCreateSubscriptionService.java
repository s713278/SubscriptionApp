package com.app.services;

import com.app.entites.Customer;
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
    private final ServiceManager serviceManager;
    private final RepositoryManager repoManager;
    private final SubscriptionServiceHelper serviceHelper;

    protected void preSubscription(Long userId,SubscriptionRequest request) {
        if(serviceManager.getSubscriptionService().
                fetchByUserIdAndVendorPriceId(userId,
                        request.getVendorPriceId())){
            throw new APIException(APIErrorCode.API_409,"Subscription already existed.Please update the subscription..");
        }
    }

    protected void postSubscription(Subscription subscription) {
           // Notify the customer after subscription creation
        notifyCustomer(subscription);
    }
    @Transactional
    public SubscriptionResponse createSubscription(Long userId,SubscriptionRequest request) {
        log.info("Start - Create subscription request for customer {}",userId);
        Subscription subscription = new Subscription();
            preSubscription(userId,request);
            // Fetch customer and tenant info
            Customer customer = serviceManager.getUserService().fetchUserById(userId);
            // Set SKU, quantity, and frequency
            Vendor vendor=serviceManager.getVendorService().fetchVendor(request.getVendorPriceId());
          //  subscription.setCustomer(customer);
        subscription.setUserId(userId);
        subscription.setVendorPriceId(request.getVendorPriceId());
            subscription.setStatus(SubscriptionStatus.NEW);
          //  subscription.setSku(sku);
            subscription.setQuantity(request.getQuantity());
            subscription.setFrequency(request.getFrequency());
          //  subscription.setVendor(vendor);
            if (request.getFrequency() == SubscriptionFrequency.CUSTOM) {
                subscription.setCustomDays(request.getCustomDays());
            }
            subscription.setStartDate(request.getStartDate());
            subscription.setNextDeliveryDate(serviceHelper.calculateNextDeliveryDate(subscription));
            subscription.setUpdateVersion(1); //Created First Time
            subscription = repoManager.getSubscriptionRepo().save(subscription);
            postSubscription(subscription);
        // Create the Data object with subscription_id and next_delivery_date
        SubscriptionResponse.Data data = new SubscriptionResponse.Data(subscription.getId(),
                subscription.getNextDeliveryDate());
        log.info("End - Create subscription request for user {}",userId);
        return new SubscriptionResponse(true, "Subscription created successfully", data);
    }


    protected abstract void notifyCustomer(Subscription subscription);
}
