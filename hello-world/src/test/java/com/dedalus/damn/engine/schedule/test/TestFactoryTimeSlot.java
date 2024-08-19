package com.dedalus.damn.engine.schedule.test;

import com.dedalus.damn.engine.schedule.domain.TimeSlot;
import com.dedalus.damn.engine.schedule.factory.FactoryTimeSlot;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import java.util.List;

public class TestFactoryTimeSlot {

    @Test
    void createTimeSlots() {
        List<TimeSlot> timeslots = FactoryTimeSlot.create(1, 2, 11, 18, 7, 3);
        assertThat(timeslots.size()).isEqualTo(3);
        assertThat(timeslots.get(0).getStart().toString()).isEqualTo("2024-01-02T11:00");
        assertThat(timeslots.get(0).getEnd().toString()).isEqualTo("2024-01-02T18:00");
        assertThat(timeslots.get(1).getStart().toString()).isEqualTo("2024-01-09T11:00");
        assertThat(timeslots.get(1).getEnd().toString()).isEqualTo("2024-01-09T18:00");
        assertThat(timeslots.get(2).getStart().toString()).isEqualTo("2024-01-16T11:00");
        assertThat(timeslots.get(2).getEnd().toString()).isEqualTo("2024-01-16T18:00");


        timeslots = FactoryTimeSlot.create(12, 2, 13);
        assertThat(timeslots.size()).isEqualTo(13);
        assertThat(timeslots.get(0).getStart().toString()).isEqualTo("2024-01-12T00:00");
        assertThat(timeslots.get(0).getEnd().toString()).isEqualTo("2024-01-12T23:00");
        assertThat(timeslots.get(1).getStart().toString()).isEqualTo("2024-01-14T00:00");
        assertThat(timeslots.get(1).getEnd().toString()).isEqualTo("2024-01-14T23:00");
    }

    @Test
    void createTimeSlotsFromDays() {
        List<TimeSlot> timeslots = FactoryTimeSlot.createFromDays(new int[]{4,5,6,9});
        assertThat(timeslots.size()).isEqualTo(4);
        assertThat(timeslots.get(0).getStart().toString()).isEqualTo("2024-01-04T00:00");
        assertThat(timeslots.get(0).getEnd().toString()).isEqualTo("2024-01-04T23:00");
        assertThat(timeslots.get(1).getStart().toString()).isEqualTo("2024-01-05T00:00");
        assertThat(timeslots.get(1).getEnd().toString()).isEqualTo("2024-01-05T23:00");
        assertThat(timeslots.get(2).getStart().toString()).isEqualTo("2024-01-06T00:00");
        assertThat(timeslots.get(2).getEnd().toString()).isEqualTo("2024-01-06T23:00");
        assertThat(timeslots.get(3).getStart().toString()).isEqualTo("2024-01-09T00:00");
        assertThat(timeslots.get(3).getEnd().toString()).isEqualTo("2024-01-09T23:00");

        timeslots = FactoryTimeSlot.createFromDaysAndHours(new String[]{ "10,8,22", "14,1,23"});
        assertThat(timeslots.size()).isEqualTo(2);
        assertThat(timeslots.get(0).getStart().toString()).isEqualTo("2024-01-10T08:00");
        assertThat(timeslots.get(0).getEnd().toString()).isEqualTo("2024-01-10T22:00");
        assertThat(timeslots.get(1).getStart().toString()).isEqualTo("2024-01-14T01:00");
        assertThat(timeslots.get(1).getEnd().toString()).isEqualTo("2024-01-14T23:00");
    }


}
