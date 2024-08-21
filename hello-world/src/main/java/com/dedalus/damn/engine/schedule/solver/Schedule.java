package com.dedalus.damn.engine.schedule.solver;

import com.dedalus.damn.engine.schedule.domain.Appointment;
import com.dedalus.damn.engine.schedule.domain.Resource;
import com.dedalus.damn.engine.schedule.domain.TaggableResource;
import com.dedalus.damn.engine.schedule.domain.TimeConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@PlanningSolution
@Getter
@Setter
@NoArgsConstructor
public class Schedule {

    private Date startDate = new Date();
    private int endDays = 90;
    private int granularityMinutes = 60;

    @ProblemFactCollectionProperty
    public List<TimeConstraint> timeConstraints;

    @PlanningEntityCollectionProperty
    private List<Appointment> appointmentList;

    @PlanningScore
    private HardSoftScore score;

//    @ProblemFactCollectionProperty
//    private List<TaggableResource> dynamicResources;

    public Schedule(List<TimeConstraint> timeConstraints, List<Appointment> appointmentList){ // }, List<TaggableResource> dynamicResources) {
        this.timeConstraints = timeConstraints;
        this.appointmentList = appointmentList;
        //this.dynamicResources = dynamicResources;
    }


    @ValueRangeProvider(id = "timeRange")
    public List<LocalDateTime> getTimeRange() {
        List<LocalDateTime> timeRange = new ArrayList<>();
        LocalDateTime startDate = this.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime start = startDate.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime end = start.plusDays(endDays);

        while (!start.isAfter(end)) {
            if (start.getHour() > 6 && start.getHour() < 22) {
                timeRange.add(start);
            }
            start = start.plusMinutes(granularityMinutes);
        }

        return timeRange;
    }

//    @ValueRangeProvider(id = "bestCommonResource1")
//    public List<TaggableResource> getDynamicTimeRange() {
//        return this.dynamicResources;
//    }

}
