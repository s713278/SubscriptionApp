package com.app.services;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.entites.Subscription;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.SubscriptionDTO;
import com.app.payloads.request.SubscriptionStatusRequest;
import com.app.payloads.request.UpdateSubscriptionRequest;
import com.app.payloads.response.SubscriptionResponse;
import com.app.repositories.RepositoryManager;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final RepositoryManager repoManager;
    private final SubscriptionServiceHelper serviceHelper;
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
        
        if(!Objects.equals(request.getCustomerId(), subscription.getCustomer().getId())) {
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
            subscription.setNextDeliveryDate(serviceHelper.calculateNextDeliveryDate(subscription));
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
    
    /**
     * 
     * @param vendorId
     * @return
     */
    @Transactional(readOnly = true)
    public List<SubscriptionDTO> fetchSubsByVendor(final Long vendorId){
        log.debug("Start 'fetchSubscriptionsByVendorId' for vendor:{}",vendorId);
        var subs =repoManager .getSubscriptionRepo().findByVendorId(vendorId);
        if(subs==null || subs.isEmpty()) {
            return Collections.emptyList();
        }
       var result=  subs.stream().map(sub->
       new SubscriptionDTO(
                sub.getId(),
                sub.getSku().getName(),
                0,
                sub.getSku().getSize(),
                sub.getStatus(), 
                sub.getQuantity(), 
                sub.getFrequency(),
                sub.getCustomDays(), 
                sub.getFromStartDate(),
                sub.getNextDeliveryDate()))
               .toList();
       log.debug("End 'fetchSubscriptionsByVendorId' for vendor:{}",vendorId);
       return result;
    }
    
    /**
     * 
     * @param vendorId
     * @return
     */
    @Transactional(readOnly = true)
    public List<SubscriptionDTO> fetchSubsByUserAndVendor(final Long userId,final Long vendorId){
        log.debug("Start 'fetchByCustomerIdAndVendorId' for user :{} and vendor:{}",userId,vendorId);
        var subs =repoManager .getSubscriptionRepo().findByCustomerIdAndVendorId(userId,vendorId);
        if(subs==null || subs.isEmpty()) {
            return Collections.emptyList();
        }
        log.info("No of subscriptions:{} found for user :{} and vendor:{}",subs.size() , userId,vendorId);
       var result=  subs.stream().map(sub->
       new SubscriptionDTO(
                sub.getId(),
                sub.getSku().getName(),
                0,
                sub.getSku().getSize(),
                sub.getStatus(), 
                sub.getQuantity(), 
                sub.getFrequency(),
                sub.getCustomDays(), 
                sub.getFromStartDate(),
                sub.getNextDeliveryDate()))
               .toList();
       log.debug("End 'fetchByCustomerIdAndVendorId' for user :{} and vendor:{}",userId,vendorId);
       return result;
    }

    /**
     *
     * @param vendorId
     * @return
     */
    @Transactional(readOnly = true)
    public List<SubscriptionDTO> fetchSubsByUserAndVendor(final Long userId,final Long vendorId,Long skuId){
        log.debug("Start 'fetchByCustomerIdAndVendorId' for user :{} and vendor:{}",userId,vendorId);
        var subs =repoManager .getSubscriptionRepo().findByCustomerIdAndVendorId(userId,vendorId);
        if(subs==null || subs.isEmpty()) {
            return Collections.emptyList();
        }
        log.info("No of subscriptions:{} found for user :{} and vendor:{}",subs.size() , userId,vendorId);
        var result=  subs.stream().map(sub->
                        new SubscriptionDTO(
                                sub.getId(),
                                sub.getSku().getName(),
                                0,
                                sub.getSku().getSize(),
                                sub.getStatus(),
                                sub.getQuantity(),
                                sub.getFrequency(),
                                sub.getCustomDays(),
                                sub.getFromStartDate(),
                                sub.getNextDeliveryDate()))
                .toList();
        log.debug("End 'fetchByCustomerIdAndVendorId' for user :{} and vendor:{}",userId,vendorId);
        return result;
    }

    public boolean fetchByCustomerIdAndVendorIdAndSkuId(final Long userId, final Long vendorId, final Long skuId){
        return repoManager.getSubscriptionRepo().findByCustomerIdAndVendorIdAndSkuId(userId,vendorId,skuId).isPresent();
    }

}
