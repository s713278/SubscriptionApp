package com.app.services;

import com.app.entites.Subscription;
import com.app.entites.SubscriptionFrequency;
import com.app.entites.SubscriptionItem;
import com.app.entites.SubscriptionStatus;
import com.app.payloads.SubscriptionItemDto;
import com.app.payloads.request.SubscriptionRequest;
import com.app.payloads.response.SubscriptionResponse;
import com.app.repositories.CustomerRepo;
import com.app.repositories.ProductRepo;
import com.app.repositories.SubscriptionRepository;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private CustomerRepo customerRepository;

    @Autowired
    private ProductRepo productRepository;

    @Autowired
    private OrderService orderService;

    public SubscriptionResponse createSubscription(SubscriptionRequest request) {
        Subscription subscription = new Subscription();
        subscription.setCustomer(customerRepository.findById(request.getCustomerId()).orElseThrow());
        subscription.setStartDate(request.getStartDate());
        subscription.setEndDate(request.getEndDate());
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        List<SubscriptionItem> subscriptionItems = new ArrayList<>();

        for (SubscriptionItemDto itemDto : request.getItems()) {
            SubscriptionItem item = new SubscriptionItem();
            item.setProduct(productRepository.findById(itemDto.getProductId()).orElseThrow());
            item.setQuantity(itemDto.getQuantity());
            item.setFrequency(itemDto.getFrequency());

            if (itemDto.getFrequency() == SubscriptionFrequency.CUSTOM) {
                item.setCustomDays(itemDto.getCustomDays());
            }

            item.setNextDeliveryDate(calculateNextDeliveryDate(item));
            subscriptionItems.add(item);
        }

        subscription.setSubscriptionItems(subscriptionItems);
        subscriptionRepository.save(subscription);

        // Create orders for one-time and recurring items
        for (SubscriptionItem item : subscriptionItems) {
            orderService.createInitialOrder(item);
        }

        // Notify the customer after subscription creation
        notifyCustomer(subscription);

        
        return null;
    }

    private LocalDate calculateNextDeliveryDate(SubscriptionItem item) {
        switch (item.getFrequency()) {
        case DAILY:
            return LocalDate.now().plusDays(1);
        case WEEKLY:
            return LocalDate.now().plusWeeks(1);
        case CUSTOM:
            return findNextCustomDeliveryDate(item);
        default:
            return LocalDate.now();
        }
    }

    private LocalDate findNextCustomDeliveryDate(SubscriptionItem subscription) {
        // Logic to find the next custom day in the week (e.g., Mon, Wed, Fri)
        List<Integer> customDays = subscription.getCustomDays();
        LocalDate today = LocalDate.now();
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

    public List<Subscription> getSubscriptionsByCustomerId(Long customerId) {
        // TODO Auto-generated method stub
        return null;
    }
}
