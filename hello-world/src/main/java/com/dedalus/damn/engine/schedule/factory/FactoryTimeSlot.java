package com.dedalus.damn.engine.schedule.factory;

import com.dedalus.damn.engine.schedule.domain.TimeSlot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FactoryTimeSlot {

    public static TimeSlot create(int day) {
        return new TimeSlot(LocalDateTime.of(2024, 1, day, 0, 0), LocalDateTime.of(2024, 1, day, 23, 0));
    }

    public static TimeSlot create(int month, int day, int fromH, int toH) {
        return new TimeSlot(LocalDateTime.of(2024, month, day, fromH, 0), LocalDateTime.of(2024, month, day, toH, 0));
    }

    public static List<TimeSlot> createFromDaysAndHours(String[] daysHours) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        for (int i = 0; i < daysHours.length; i++) {
            int[] daysHoursInstance = Arrays.stream(daysHours[i].split(",")).map(s -> s.trim()).mapToInt(Integer::parseInt).toArray();
            timeSlots.add(new TimeSlot(LocalDateTime.of(2024, 1, daysHoursInstance[0], daysHoursInstance[1], 0), LocalDateTime.of(2024, 1, daysHoursInstance[0], daysHoursInstance[2], 0)));
        }
        return timeSlots;
    }

    public static List<TimeSlot> createFromDays(int[] days) {
        String[] daysHours = new String[days.length];
        for (int i = 0; i < days.length; i++) {
            daysHours[i] = days[i] + ",0,23";
        }
        return createFromDaysAndHours(daysHours);
    }

    public static List<TimeSlot> create(int day, int intervalDays, int numOccurrences) {
        return create(1, day, 0, 23, intervalDays, numOccurrences);
    }

    public static List<TimeSlot> create(int month, int day, int fromH, int toH, int intervalDays, int numOccurrences) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        LocalDateTime start = LocalDateTime.of(2024, month, day, fromH, 0);
        for (int i = 0; i < numOccurrences; i++) {
            LocalDateTime end = start.plusHours(toH - fromH);
            timeSlots.add(new TimeSlot(start, end));
            start = start.plusDays(intervalDays);
        }
        return timeSlots;
    }

}
