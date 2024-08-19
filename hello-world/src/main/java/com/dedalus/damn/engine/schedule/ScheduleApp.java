package com.dedalus.damn.engine.schedule;


import com.dedalus.damn.engine.schedule.domain.*;
import com.dedalus.damn.engine.schedule.factory.FactoryResource;
import com.dedalus.damn.engine.schedule.factory.FactoryTimeSlot;
import com.dedalus.damn.engine.schedule.solver.AppointmentConstraintProvider;
import com.dedalus.damn.engine.schedule.solver.Schedule;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
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

        Resource resCal1 = FactoryResource.create("Calendar One", FactoryTimeSlot.createFromDaysAndHours(new String[]{"1, 1, 22", "2, 12, 23", "4, 16, 18"}));
        Resource resCal2 = FactoryResource.create("Calendar Two", FactoryTimeSlot.createFromDays(new int[]{3, 5}));

        long id = 0;
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment(id++, "App A", 120, Arrays.asList(resCal1, resCal2), null, null, "doctor"));   //NA: 1,3,5
        appointments.add(new Appointment(id++, "App Surgery", 240, Arrays.asList(resCal2), null, null, "doctor"));      //NA: 3,5

        List<TimeConstraint> timeConstraints = new ArrayList<>();
        timeConstraints.add(new TimeConstraint(appointments.get(0), appointments.get(1), Duration.ofDays(4), Duration.ofDays(5))); //2->(6,7)

        List<TaggableResource> docs = new ArrayList<>();
        TaggableResource resDoctor = new TaggableResource(id++, "Doctor John", FactoryTimeSlot.createFromDays(new int[]{2, 5}), "doctor");
        TaggableResource resDoctor2 = new TaggableResource(id++, "Doctor Mario", FactoryTimeSlot.createFromDays(new int[]{1}), "doctor");
        TaggableResource nurse = new TaggableResource(id++, "Nurse Maria", FactoryTimeSlot.create(1, 1, 30), "nurse");
        docs.add(resDoctor);
        docs.add(resDoctor2);
        docs.add(nurse);

        Schedule schedule = new Schedule(timeConstraints, appointments, docs);
        return schedule;
    }

    private static void printTimetable(Schedule cp) {
        LOGGER.info("");
        for (Appointment appointment: cp.getAppointmentList()) {
            LOGGER.info(appointment.toString());
        }

    }

}
