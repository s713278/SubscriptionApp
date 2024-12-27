package com.app.services;

import com.app.entites.Subscription;
import com.app.exceptions.APIErrorCode;
import com.app.exceptions.APIException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionServiceHelper {

  public LocalDate calculateNextDeliveryDate(Subscription item) {
    return switch (item.getFrequency()) {
      case DAILY -> item.getStartDate().plusDays(1);
      case ALTERNATE_DAY -> item.getStartDate().plusDays(2);
      case WEEKLY -> item.getStartDate().plusWeeks(1);
      case CUSTOM -> findNextCustomDeliveryDate(item);
      case ONE_TIME -> item.getStartDate();
      default ->
          throw new APIException(
              APIErrorCode.API_400, "Invalid Subscription Frequency " + item.getFrequency());
    };
  }

  private LocalDate findNextCustomDeliveryDate(Subscription subscription) {
    // Logic to find the next custom day in the week (e.g., Mon, Wed, Fri)
    List<Integer> customDays = subscription.getCustomDays();
    LocalDate today = subscription.getStartDate();
    int todayDayOfWeek = today.getDayOfWeek().getValue();

    for (Integer day : customDays) {
      if (day > todayDayOfWeek) {
        return today.with(TemporalAdjusters.next(DayOfWeek.of(day)));
      }
    }
    return today.with(TemporalAdjusters.next(DayOfWeek.of(customDays.get(0))));
  }
}
