package com.app.services;

import com.app.entites.Subscription;
import com.app.repositories.RepositoryManager;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

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
