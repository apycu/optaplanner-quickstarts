package com.dedalus.damn.engine.schedule.domain;

import java.time.LocalDateTime;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class TimeSlot {
    private LocalDateTime start;
    private LocalDateTime end;

    // Getters and setters
}
