package com.app.services;

import com.app.payloads.request.CreateSubscriptionRequest;
import com.app.payloads.response.SubscriptionCreateResponse;

public interface SubscriptionCreateService {
  SubscriptionCreateResponse createSubscription(Long userId, CreateSubscriptionRequest request);
}
