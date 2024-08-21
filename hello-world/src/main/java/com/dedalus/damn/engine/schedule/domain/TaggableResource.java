package com.dedalus.damn.engine.schedule.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString
public class TaggableResource extends Resource {

    private String tag;

    public TaggableResource(String id, String name, List<TimeSlot> unavailableTimeSlots, List<TimeSlot> availableTimeSlots, String tag) {
        super(id, name, unavailableTimeSlots, availableTimeSlots);
        this.tag = tag;
    }
}
