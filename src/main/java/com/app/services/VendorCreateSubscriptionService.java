package com.app.services;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.app.entites.Subscription;
import com.app.repositories.RepositoryManager;

@Service
@Primary
public class VendorCreateSubscriptionService extends AbstractCreateSubscriptionService {

    public VendorCreateSubscriptionService(RepositoryManager repoManager, SubscriptionServiceHelper serviceHelper) {
        super(repoManager, serviceHelper);
    }

    @Override
    protected void notifyCustomer(Subscription subscription) {

    }

}
