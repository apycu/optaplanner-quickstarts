package com.dedalus.damn.engine.schedule.domain;

import lombok.*;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.time.LocalDateTime;
import java.util.List;

@PlanningEntity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @PlanningId
    private Long id;

    private String name;

    private Integer durationMinutes;

    private List<Resource> requiredResources;

    private List<TimeConstraint> timeConstraints;

    @PlanningVariable(valueRangeProviderRefs = "timeRange")
    private LocalDateTime startTime;

    @Override
    public String toString() {
        return String.format("Appointment(%s - %s - %s)", getName(), getStartTime(), getDurationMinutes());
    }
}
