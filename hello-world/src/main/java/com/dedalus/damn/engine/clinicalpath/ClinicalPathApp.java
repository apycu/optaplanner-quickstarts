package com.dedalus.damn.engine.clinicalpath;

import com.dedalus.damn.engine.clinicalpath.domain.Appointment;
import com.dedalus.damn.engine.clinicalpath.domain.ClinicalPath;
import com.dedalus.damn.engine.clinicalpath.domain.Slot;
import com.dedalus.damn.engine.clinicalpath.domain.Unavailability;
import com.dedalus.damn.engine.clinicalpath.solver.ClinicalPathConstraintProvider;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.config.solver.SolverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClinicalPathApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClinicalPathApp.class);

    public static void main(String[] args) {
        SolverFactory<ClinicalPath> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(ClinicalPath.class)
                .withEntityClasses(Appointment.class)
                .withConstraintProviderClass(ClinicalPathConstraintProvider.class)
                .withTerminationSpentLimit(Duration.ofSeconds(5)));

        // Load the problem
        ClinicalPath problem = generateDemoData();

        // Solve the problem
        Solver<ClinicalPath> solver = solverFactory.buildSolver();
        ClinicalPath solution = solver.solve(problem);

        // Visualize the solution
        printTimetable(solution);
    }

    public static ClinicalPath generateDemoData() {
        List<Slot> slots = new ArrayList<>();
        LocalDateTime startDate = LocalDateTime.of(2024, 1, 1, 8, 0);
        for (int i = 0; i < 7; i++) {
            LocalDateTime date = startDate.plusDays(i);
            slots.add(new Slot(date, date.plusHours(4))); // 8 to 12
            slots.add(new Slot(date.plusHours(5), date.plusHours(9))); // 13 to 17
        }

        List<Unavailability> unavailabilities = new ArrayList<>();
        unavailabilities.add(new Unavailability(LocalDateTime.of(2024, 1, 1, 8, 0), LocalDateTime.of(2024, 1, 1, 22, 0)));
        unavailabilities.add(new Unavailability(LocalDateTime.of(2024, 1, 2, 10, 0), LocalDateTime.of(2024, 1, 2, 14, 0)));
        unavailabilities.add(new Unavailability(LocalDateTime.of(2024, 1, 4, 10, 0), LocalDateTime.of(2024, 1, 4, 14, 0)));

        long id = 0;
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(new Appointment(++id, "Appointment A", null));
        appointments.add(new Appointment(++id, "Appointment B", null));

        return new ClinicalPath(slots, appointments, unavailabilities);
    }

    private static void printTimetable(ClinicalPath cp) {
        LOGGER.info("");
        for (Appointment appointment: cp.getAppointments()) {
            LOGGER.info(appointment.toString());
        }

    }

}
