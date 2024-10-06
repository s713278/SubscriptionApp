package com.app.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Service;

import com.app.entites.Customer;
import com.app.entites.Sku;
import com.app.entites.Subscription;
import com.app.entites.SubscriptionFrequency;
import com.app.entites.SubscriptionStatus;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.SubscriptionRequest;
import com.app.payloads.request.SubscriptionStatusRequest;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.payloads.response.SubscriptionResponse;
import com.app.repositories.RepositoryManager;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final RepositoryManager repoManager;

    @Transactional
    public SubscriptionResponse createSubscription(SubscriptionRequest request) {
        log.info("Start - Create subscription request for customer {}",request.getCustomerId());
        Subscription subscription = new Subscription();
        try {
            // Fetch customer and tenant info
            Customer customer = repoManager.getCustomerRepo().findById(request.getCustomerId())
                    .orElseThrow(() ->new APIException(APIErrorCode.API_400, "Customer not found"));

            // Set SKU, quantity, and frequency
            Sku sku = repoManager.getSkuRepo().findById(request.getSkuId())
                    .orElseThrow(() -> new APIException(APIErrorCode.API_400,"SKU not found"));

            subscription.setCustomer(customer);
            subscription.setStatus(SubscriptionStatus.ACTIVE);
            subscription.setSku(sku);
            subscription.setQuantity(request.getQuantity());
            subscription.setFrequency(request.getFrequency());

            if (request.getFrequency() == SubscriptionFrequency.CUSTOM) {
                subscription.setCustomDays(request.getCustomDays());
            }

            subscription.setFromStartDate(request.getFromStartDate());
            subscription.setNextDeliveryDate(calculateNextDeliveryDate(subscription));
            subscription.setUpdateVersion(1); //Created First Time
            subscription = repoManager.getSubscriptionRepo().save(subscription);
            // Notify the customer after subscription creation
            notifyCustomer(subscription);
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

    private LocalDate calculateNextDeliveryDate(Subscription item) {
        switch (item.getFrequency()) {
        case ONE_TIME:
            return item.getFromStartDate();
        case DAILY:
            return item.getFromStartDate().plusDays(1);
        case ALTERNATE_DAY:
            return item.getFromStartDate().plusDays(2);
        case WEEKLY:
            return item.getFromStartDate().plusWeeks(1);
        case CUSTOM:
            return findNextCustomDeliveryDate(item);
        default:
            return item.getFromStartDate();
        }
    }

    private LocalDate findNextCustomDeliveryDate(Subscription subscription) {
        // Logic to find the next custom day in the week (e.g., Mon, Wed, Fri)
        List<Integer> customDays = subscription.getCustomDays();
        LocalDate today = subscription.getFromStartDate();
        int todayDayOfWeek = today.getDayOfWeek().getValue();

        for (Integer day : customDays) {
            if (day > todayDayOfWeek) {
                return today.with(TemporalAdjusters.next(DayOfWeek.of(day)));
            }
        }
        return today.with(TemporalAdjusters.next(DayOfWeek.of(customDays.get(0))));
    }

    private void notifyCustomer(Subscription subscription) {
        // Send email or SMS notification to the customer
    }

    public List<Subscription> getSubscriptionsByVendorIdAndCustomerId(Long subscriptionId, Long customerId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Transactional
    public SubscriptionResponse updateSubscription(@Valid UpdateSubscriptionRequest request) {
        log.info("Start - Update subscription request for customer {}",request.getCustomerId());
        var subscription = repoManager.getSubscriptionRepo().findById(request.getSubscriptionId())
                .orElseThrow(() -> new APIException(APIErrorCode.API_400, "Subscription not found"));
        
        if(request.getCustomerId()!=subscription.getCustomer().getId()) {
            throw new APIException(APIErrorCode.API_401, "Invalid customer id!!"); 
        }
        boolean changedFound= false;
        if(request.getFrequency()!=null && request.getFrequency()!=subscription.getFrequency()) {
            log.info("Change found in frequency from {} ==>> {} ",subscription.getFrequency(),request.getFrequency());
            subscription.setFrequency(request.getFrequency());
            changedFound = true;
        }
        if(request.getQuantity()!=null && request.getQuantity()!=subscription.getQuantity()) {
            log.info("Change found in quantity from {} ==>> {} ",subscription.getQuantity(),request.getQuantity());
            subscription.setQuantity(request.getQuantity());
            changedFound = true;
        }
        if(request.getStartDate()!=null && !request.getStartDate().equals(subscription.getFromStartDate())) {
            log.info("Change found in start date from {} ==> {} ",subscription.getFromStartDate(),request.getStartDate());
            subscription.setFromStartDate(request.getStartDate());
            subscription.setNextDeliveryDate(calculateNextDeliveryDate(subscription));
            changedFound = true;
        }
        SubscriptionResponse response =new SubscriptionResponse(true, "No subscription changes found!!");
        if(changedFound) {
            subscription.setUpdateVersion(subscription.getUpdateVersion()+1);
            repoManager.getSubscriptionRepo().save(subscription);
            response=new SubscriptionResponse(true, "Subscription updated successfully");
        }
        log.info("End - Update subscription request for customer {}",request.getCustomerId());
        return response;
    }
    
    @Transactional
    public SubscriptionResponse updateSubscriptionStatus(SubscriptionStatusRequest request) {
        log.info("Start - Update subscription_status request for customer {}",request.getCustomerId());
        var subscription = repoManager.getSubscriptionRepo().findById(request.getSubscriptionId())
                .orElseThrow(() -> new APIException(APIErrorCode.API_400, "Subscription not found"));
        
        if(request.getCustomerId()!=subscription.getCustomer().getId()) {
            throw new APIException(APIErrorCode.API_401, "Invalid customer id!!"); 
        }
        SubscriptionResponse response = new SubscriptionResponse(true, "No subscription status changes found!!");
        if(request.getStatus()!=null && request.getStatus()!=subscription.getStatus()) {
            log.info("Change found in frequency from {} ==>> {} ",subscription.getStatus(),request.getStatus());
            subscription.setStatus(request.getStatus());
            subscription.setUpdateVersion(subscription.getUpdateVersion()+1);
            repoManager.getSubscriptionRepo().save(subscription);
            response = new SubscriptionResponse(true, "Subscription status updated successfully");
        }
        log.info("End - Update subscription_status request for customer {}",request.getCustomerId());
        return response;
        
    }

    public void deleteSubscription(Long subscriptionId) {
        // TODO Auto-generated method stub

    }
    
    public Subscription fetchSubscription(final Long subId) {
        Subscription sub =repoManager.getSubscriptionRepo().findById(subId)
                .orElseThrow(()-> new APIException(APIErrorCode.API_400, "Subsction details not found"));
        return sub;
    }
}
