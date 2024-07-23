package com.dedalus.damn.engine.clinicalpath.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
@Getter @Setter @NoArgsConstructor
public class ClinicalPath {

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Slot> slots;

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Unavailability> unavailabilities;

    @PlanningEntityCollectionProperty
    private List<Appointment> appointments;

    @PlanningScore
    private HardSoftScore score;

    public ClinicalPath(List<Slot> slots, List<Appointment> appointments, List<Unavailability> unavailabilities) {
        this.slots = slots;
        this.appointments = appointments;
        this.unavailabilities = unavailabilities;
    }


}
