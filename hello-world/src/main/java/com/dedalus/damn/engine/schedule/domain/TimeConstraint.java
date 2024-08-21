package com.dedalus.damn.engine.schedule.domain;
import lombok.*;

import java.time.Duration;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class TimeConstraint {

    private String earlierAppointmentId;
    private String laterAppointmentId;
    private Duration minDuration;
    private Duration maxDuration;

}
