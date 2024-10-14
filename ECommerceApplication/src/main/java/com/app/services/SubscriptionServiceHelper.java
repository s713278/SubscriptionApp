package com.app.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.stereotype.Component;

import com.app.entites.Subscription;

@Component
public class SubscriptionServiceHelper {

    public LocalDate calculateNextDeliveryDate(Subscription item) {
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
}
