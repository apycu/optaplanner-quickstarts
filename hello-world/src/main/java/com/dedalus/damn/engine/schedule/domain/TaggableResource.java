package com.dedalus.damn.engine.schedule.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter @NoArgsConstructor @ToString
public class TaggableResource extends Resource {

    private String tag;

    public TaggableResource(Long id, String name, List<TimeSlot> unavailableTimeSlots, String tag) {
        super(id, name, unavailableTimeSlots);
        this.tag = tag;
    }
}
