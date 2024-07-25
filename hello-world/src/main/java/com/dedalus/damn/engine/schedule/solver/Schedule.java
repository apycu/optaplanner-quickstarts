package com.dedalus.damn.engine.schedule.solver;

import com.dedalus.damn.engine.schedule.domain.Appointment;
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
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@PlanningSolution
@Getter
@Setter
@NoArgsConstructor
public class Schedule {

    @ProblemFactCollectionProperty
    public List<TimeConstraint> timeConstraints;

    @PlanningEntityCollectionProperty
    private List<Appointment> appointmentList;

    @PlanningScore
    private HardSoftScore score;

    public Schedule(List<TimeConstraint> timeConstraints, List<Appointment> appointmentList) {
        this.timeConstraints = timeConstraints;
        this.appointmentList = appointmentList;
    }

    @ValueRangeProvider(id = "timeRange")
    public List<LocalDateTime> getTimeRange() {
        List<LocalDateTime> timeRange = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 7, 0);
        LocalDateTime start = startDate.truncatedTo(ChronoUnit.DAYS);
        LocalDateTime end = start.plusDays(7);

        while (!start.isAfter(end)) {
            if (start.getHour() > 6 && start.getHour() < 19) {
                timeRange.add(start);
            }
            start = start.plusMinutes(60);
        }

        return timeRange;
    }

}
