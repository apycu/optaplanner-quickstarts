package com.dedalus.damn.engine.schedule.domain;
import lombok.*;

import java.time.Duration;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class TimeConstraint {

    private Appointment earlierAppointment;
    private Appointment laterAppointment;
    private Duration minDuration;
    private Duration maxDuration;

}
