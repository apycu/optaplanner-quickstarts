package com.dedalus.damn.engine.clinicalpath.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;


@PlanningEntity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Appointment {

    @PlanningId
    private long id;

    private String name;

    @PlanningVariable
    private Slot slot;

}
