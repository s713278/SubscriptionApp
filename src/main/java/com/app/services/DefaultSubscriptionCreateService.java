package com.app.services;

import com.app.repositories.RepositoryManager;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DefaultSubscriptionCreateService extends AbstractSubscriptionCreateService {

  public DefaultSubscriptionCreateService(
      ServiceManager serviceManager,
      RepositoryManager repoManager,
      SubscriptionServiceHelper serviceHelper) {
    super(serviceManager, repoManager, serviceHelper);
  }

  @Async
  @Override
  protected void updateInventory(Long skuId, Integer quantity) {}
}
