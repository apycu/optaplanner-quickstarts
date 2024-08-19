package com.dedalus.damn.engine.schedule.solver;

import com.dedalus.damn.engine.schedule.domain.Appointment;
import com.dedalus.damn.engine.schedule.domain.Resource;
import com.dedalus.damn.engine.schedule.domain.TimeConstraint;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.Joiners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class AppointmentConstraintProvider implements ConstraintProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentConstraintProvider.class);

    @Override
    public Constraint[] defineConstraints(ConstraintFactory factory) {
        return new Constraint[]{
                resourceAvailabilityConstraint(factory),
                timeConstraint(factory),
                sameDynamic(factory)
        };
    }

    private Constraint resourceAvailabilityConstraint(ConstraintFactory factory) {
        return factory.forEach(Appointment.class)
                .filter(appointment -> !areResourcesAvailable(appointment))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Resource not available");
    }

    private Constraint timeConstraint(ConstraintFactory factory) {
        return factory.forEach(TimeConstraint.class)
                .join(Appointment.class,
                        Joiners.equal(timeConstraint -> timeConstraint.getEarlierAppointment().getId(), appointment -> appointment.getId()))
                .join(Appointment.class,
                        Joiners.equal((timeConstraint, earlierAppointment) -> timeConstraint.getLaterAppointment().getId(), appointment -> appointment.getId()))
                .filter((timeConstraint, earlierAppointment, laterAppointment) -> {
                    return !isTimeConstraintSatisfied(timeConstraint, earlierAppointment, laterAppointment);
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Time constraint conflict");
    }

    private Constraint sameDynamic(ConstraintFactory factory) {
        return factory.forEachUniquePair(Appointment.class, Joiners.equal(Appointment::getBestCommonResource1Name))
                .filter((appointment, appointment2) -> {
                    if (!appointment.getBestCommonResource1().getTag().equals(appointment.getBestCommonResource1Name())){
                        return true;
                    }
                    if (!isResourceAvailable(appointment, appointment.getBestCommonResource1())) {
                        return true;
                    }
                    // System.out.println("OOOHH " + appointment + " " + appointment2);
                    return !appointment.getBestCommonResource1().getId().equals(appointment2.getBestCommonResource1().getId());
                })
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Dynamic value not the same");
    }

    public boolean areResourcesAvailable(Appointment appointment) {
        AtomicBoolean available = new AtomicBoolean(true);
        appointment.getRequiredResources().stream().forEach(resource -> {
            resource.getUnavailableTimeSlots().stream().forEach(ts -> {
                available.set(available.get() && isResourceAvailable(appointment, resource));
            });
        });
        return available.get();
    }

    public boolean isResourceAvailable(Appointment appointment, Resource resource) {
        AtomicBoolean available = new AtomicBoolean(true);
        resource.getUnavailableTimeSlots().stream().forEach(ts -> {
            available.set(available.get() && !isOverlapping(appointment.getStartTime(), appointment.getStartTime().plusMinutes(appointment.getDurationMinutes()), ts.getStart(), ts.getEnd()));
        });
        return available.get();
    }

    public boolean isTimeConstraintSatisfied(TimeConstraint timeConstraint, Appointment earlierAppointment, Appointment laterAppointment) {
        LocalDateTime earlierStart = earlierAppointment.getStartTime();
        LocalDateTime laterStart = laterAppointment.getStartTime();

        if (earlierStart == null || laterStart == null) {
            return false;
        }

        Duration actualDuration = Duration.between(earlierStart, laterStart);
        return actualDuration.compareTo(timeConstraint.getMinDuration()) >= 0 &&
                actualDuration.compareTo(timeConstraint.getMaxDuration()) <= 0;
    }

    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        // return !start1.isAfter(end2) && !start2.isAfter(end1);
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

}
