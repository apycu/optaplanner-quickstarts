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

    @PlanningVariable(valueRangeProviderRefs = "timeRange")
    private LocalDateTime startTime;

    @PlanningVariable(valueRangeProviderRefs = "bestCommonResource1")
    private TaggableResource bestCommonResource1;

    private String bestCommonResource1Name;

    @Override
    public String toString() {
        return String.format("Appointment(%s - %s - %s - %s)", getName(), getStartTime(), getDurationMinutes(), bestCommonResource1 == null ? "" : bestCommonResource1.getName());
    }
}
