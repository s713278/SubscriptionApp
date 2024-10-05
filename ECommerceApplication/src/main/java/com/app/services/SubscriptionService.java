package com.app.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Customer;
import com.app.entites.Sku;
import com.app.entites.Subscription;
import com.app.entites.SubscriptionFrequency;
import com.app.entites.SubscriptionStatus;
import com.app.payloads.SubscriptionItemDto;
import com.app.payloads.request.SubscriptionRequest;
import com.app.payloads.response.SubscriptionResponse;
import com.app.repositories.CustomerRepo;
import com.app.repositories.SkuRepo;
import com.app.repositories.SubscriptionRepository;
import com.app.repositories.VendorSkuPriceRepo;

@Service
public class SubscriptionService {

	@Autowired
	private SubscriptionRepository subscriptionRepository;

	@Autowired
	private CustomerRepo customerRepository;

	@Autowired
	private SkuRepo skuRepo;

	private VendorSkuPriceRepo vendorSkuPriceRepo;

	@Autowired
	private OrderService orderService;

	public SubscriptionResponse createSubscription(SubscriptionRequest request) {

		// Fetch customer and tenant info
		Customer customer = customerRepository.findById(request.getCustomerId())
				.orElseThrow(() -> new RuntimeException("Customer not found"));

		// Set SKU, quantity, and frequency
		Sku sku = skuRepo.findById(request.getSkuId()).orElseThrow(() -> new RuntimeException("SKU not found"));

		Subscription subscription = new Subscription();
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

		// subscription.setSubscriptionItems(subscriptionItems);
		subscriptionRepository.save(subscription);
		// Notify the customer after subscription creation
		notifyCustomer(subscription);

		return new SubscriptionResponse(true,null);
	}

	private LocalDate calculateNextDeliveryDate(Subscription item) {
		switch (item.getFrequency()) {
		case DAILY:
			return item.getFromStartDate().plusDays(1);
		case ALTERNATE_DAY:
			return item.getFromStartDate().plusDays(2);
		case WEEKLY:
			return item.getFromStartDate().plusWeeks(1);
		case CUSTOM:
			return findNextCustomDeliveryDate(item);
		default:
			return LocalDate.now();
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

	public List<Subscription> getSubscriptionsByCustomerId(Long customerId) {
		// TODO Auto-generated method stub
		return null;
	}
}
