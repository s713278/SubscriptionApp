package com.app.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.app.entites.*;
import com.app.entites.type.SkuType;
import org.springframework.scheduling.annotation.Async;

import com.app.config.AppConstants;
import com.app.constants.NotificationType;
import com.app.entites.type.SubFrequency;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import com.app.payloads.request.CreateSubscriptionRequest;
import com.app.payloads.response.SubscriptionResponseDTO;
import com.app.repositories.RepositoryManager;
import com.app.services.constants.PaymentType;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCreateSubscriptionService {
    private final ServiceManager serviceManager;
    private final RepositoryManager repoManager;
    private final SubscriptionServiceHelper serviceHelper;


    protected void isSkuAvailable(Sku sku){
        log.debug("Validating whether the SKU : {} is available or not ? {} ",sku.getId(),sku.isAvailable());
        if(!sku.isAvailable()){
            throw new APIException(APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,String.format("SKU %s is out of stock",sku.getId()));
        }
    }
    /**
     * <ol>
     *     <li>Verifying the duplicate subscription request</li>
     *      <li>Verify the requested frequency is existed in DB or not</li>
     *     <li>Delivery date validation for ONE_TIME frequency</li>
     * </ol>
     * @param userId
     * @param request
     */
    protected void preSubscription(Long userId, CreateSubscriptionRequest request) {
        if(serviceManager.getSubscriptionService().
                fetchByUserIdAndSkuId(userId,
                        request.getSkuId())){
            throw new APIException(APIErrorCode.API_409,"Subscription already existed for sku id : "+request.getSkuId());
        }
        var skuSub= serviceManager.getSkuSubscriptionService().fetchBySkuIdAndFrequency(request.getSkuId(),request.getFrequency());
        if(request.getFrequency() == SubFrequency.ONE_TIME){
            ObjectMapper objectMapper=new ObjectMapper();
            Map<String,Object> map = skuSub.getEligibleDeliveryDays();
            List<String> deliveryDaysList = objectMapper.convertValue(map.get(AppConstants.SUB_ONE_TIME_DELIVERY_DAYS), new TypeReference<List<String>>(){});
            log.debug("Sku Eligible Delivery Options : {}",deliveryDaysList);
                if(deliveryDaysList!=null && !deliveryDaysList.isEmpty() && "FIXED".equalsIgnoreCase((String)map.get(AppConstants.SUB_ONE_TIME_SUB_TYPE))){
                    log.debug("Day of the week :{}",request.getDeliveryDate().getDayOfWeek().name());
                    if(!deliveryDaysList
                            .contains(request.getDeliveryDate().getDayOfWeek().name())){
                        throw new APIException(APIErrorCode.API_400,"Selected delivery date is not available for this item : "+request.getSkuId() +
                                ". Please select "+skuSub.getEligibleDeliveryDays().get(AppConstants.SUB_ONE_TIME_DELIVERY_DAYS));

                    }

                }
        }
        validatePrice(userId,request);
    }

    protected void validatePrice(Long userId,CreateSubscriptionRequest request){
        log.debug("Validating is there any price change for sku :{}",request.getSkuId());
        var dbPriceId = serviceManager.getPriceService().fetchTodayPriceBySkuId(request.getSkuId());
        if(!dbPriceId.getId().equals(request.getPriceId())){
            log.warn("Price has been changed to {}  for user {}",dbPriceId.getSalePrice(),userId);
            throw new APIException(APIErrorCode.SUBSCRIPTION_VALIDATION_FAILED,
                    String.format("Price has been changed to %s",dbPriceId.getSalePrice()));
        }
    }


    @Transactional
    public SubscriptionResponseDTO createSubscription(Long userId, CreateSubscriptionRequest request) {
        log.info("Start - Create subscription request for user {}",userId);
        var sku = serviceManager.getSkuService().fetchSkuEntityById(request.getSkuId());
        isSkuAvailable(sku);
        Subscription subscription = new Subscription();
            preSubscription(userId,request);
            // Fetch customer and tenant info
            Customer customer = serviceManager.getUserService().fetchUserById(userId);
            // Set SKU, quantity, and frequency
           // Vendor vendor=serviceManager.getVendorService().fetchVendor(request.getSkuId());
          //  subscription.setCustomer(customer);
        subscription.setUserId(userId);
        subscription.setSkuId(request.getSkuId());
        //Get Latest Price from the repository
        subscription.setPriceId(request.getPriceId());
            subscription.setStatus(SubscriptionStatus.PENDING);
            subscription.setQuantity(request.getQuantity());
            subscription.setFrequency(request.getFrequency());
            switch (subscription.getFrequency()){

                case ONE_TIME ->{
                    if(sku.getSkuType() == SkuType.SERVICE){
                        subscription.setStartDate(LocalDate.now());
                        subscription.setEndDate(LocalDate.now().plusDays(sku.getServiceValidDays()));
                    }else{
                        subscription.setStartDate(request.getDeliveryDate());
                    }
                }
                case CUSTOM -> {
                    subscription.setCustomDays(request.getCustomDays());
                    subscription.setStartDate(request.getStartDate());
                    subscription.setEndDate(request.getEndDate());
                }
                 default -> {
                    subscription.setStartDate(request.getStartDate());
                    subscription.setEndDate(request.getEndDate());
                }
            }
            subscription.setNextDeliveryDate(serviceHelper.calculateNextDeliveryDate(subscription));
            subscription.setUpdateVersion(1); //Created First Time
            subscription = repoManager.getSubscriptionRepo().save(subscription);

        // Create the Data object with subscription_id and next_delivery_date
        SubscriptionResponseDTO responseDTO = new SubscriptionResponseDTO(
                subscription.getId(),
                subscription.getCreatedDate(),
                "",//vendor.getBusinessName(),
                subscription.getNextDeliveryDate(),
                PaymentType.CASH_ON_DELIVERY.name(),
                subscription.getStatus(),
                subscription.getFrequency(),
                customer.getDeliveryAddress(),
                customer.getFullMobileNumber()
        );
      //  postSubscription(responseDTO);
        notifyCustomer( customer.getFullMobileNumber(),customer.getEmail());
        notifyVendor(customer.getFullMobileNumber());
        updateInventory(request.getSkuId());
        log.info("End - Create subscription request for user {}",userId);
        return responseDTO;
    }

    @Async
    protected  void notifyCustomer(String mobile,String email){
        log.debug("Sending user notification to {} ",mobile);
        if(mobile!=null){
            serviceManager.getNotificationContext().sendOTPMessage(NotificationType.SMS,
                    mobile);
        }else if(email!=null){
            serviceManager.getNotificationContext().sendOTPMessage(NotificationType.EMAIL,email);
        }else{
            log.info("Customer has neither mobile number nor email");
        }
    }

    @Async
    protected  void notifyVendor(String mobile){
        log.debug("Sending vendor notification to {} ",mobile);
        serviceManager.getNotificationContext().sendOTPMessage(NotificationType.SMS,
                mobile);
    }

    protected abstract void updateInventory(Long skuId);
}
