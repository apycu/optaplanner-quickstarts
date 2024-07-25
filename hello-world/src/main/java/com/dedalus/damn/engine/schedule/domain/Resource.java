package com.dedalus.damn.engine.schedule.domain;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Resource {

    private Long id;

    private String name;

    private List<TimeSlot> unavailableTimeSlots;
}
