package com.dedalus.damn.engine.schedule;


import com.dedalus.damn.engine.schedule.domain.Appointment;
import com.dedalus.damn.engine.schedule.domain.Resource;
import com.dedalus.damn.engine.schedule.domain.TimeConstraint;
import com.dedalus.damn.engine.schedule.domain.TimeSlot;
import com.dedalus.damn.engine.schedule.solver.AppointmentConstraintProvider;
import com.dedalus.damn.engine.schedule.solver.Schedule;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleApp.class);

    public static void main(String[] args) {
        SolverFactory<Schedule> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(Schedule.class)
                .withEntityClasses(Appointment.class)
                .withConstraintProviderClass(AppointmentConstraintProvider.class)
                .withTerminationSpentLimit(Duration.ofSeconds(2)));

        Schedule problem = generateDemoData();

        Solver<Schedule> solver = solverFactory.buildSolver();
        Schedule solution = solver.solve(problem);

        printTimetable(solution);
    }

    public static Schedule generateDemoData() {

        long id = 0;
        Resource resCal1 = new Resource(id++, "Cal1", Arrays.asList(un(1, 1, 22), un(2, 12, 23), un(4, 16, 18)));
        Resource resCal2 = new Resource(id++, "Cal2", Arrays.asList(un(3, 1, 22), un(5, 1, 22)));

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment(++id, "App A", 120, Arrays.asList(resCal1, resCal2), null, null));
        appointments.add(new Appointment(++id, "App Surgery", 240, Arrays.asList(resCal2), null, null));

        List<TimeConstraint> timeConstraints = new ArrayList<>();
        timeConstraints.add(new TimeConstraint(appointments.get(0), appointments.get(1), Duration.ofDays(4), Duration.ofDays(5)));

        return new Schedule(timeConstraints, appointments);
    }

    private static TimeSlot un(int day, int fromH, int toH) {
        return new TimeSlot(LocalDateTime.of(2024, 1, day, fromH, 0), LocalDateTime.of(2024, 1, day, toH, 0));
    }

//    private static List<TimeSlot> unFromH(int fromH) {
//        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, fromH, 0);
//        LocalDateTime endDate = LocalDateTime.of(2024, 1, 1, 24, 0);
//        List<TimeSlot> timeSlots = new ArrayList<>();
//        for (int i = 0; i < 7; i++) {
//            timeSlots.add(new TimeSlot(startDate.plusDays(i), endDate.plusDays(1)));
//        }
//        return timeSlots;
//    }

    private static void printTimetable(Schedule cp) {
        LOGGER.info("");
        for (Appointment appointment: cp.getAppointmentList()) {
            LOGGER.info(appointment.toString());
        }

    }

}
