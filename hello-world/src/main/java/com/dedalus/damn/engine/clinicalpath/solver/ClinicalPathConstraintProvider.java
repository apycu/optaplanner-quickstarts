package com.dedalus.damn.engine.clinicalpath.solver;

import com.dedalus.damn.engine.clinicalpath.domain.Appointment;
import com.dedalus.damn.engine.clinicalpath.domain.Slot;
import com.dedalus.damn.engine.clinicalpath.domain.Unavailability;
import org.acme.schooltimetabling.domain.Lesson;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.time.Duration;
import java.time.LocalDateTime;

public class ClinicalPathConstraintProvider implements ConstraintProvider {

    public static boolean isOverlapping(Appointment app1, Appointment app2) {
        return isOverlapping(app1.getSlot(), app2.getSlot());
    }

    public static boolean isOverlapping(Appointment app1, Unavailability app2) {
        return isOverlapping(app1.getSlot().getStart(), app1.getSlot().getEnd(), app2.getStart(), app2.getEnd());
    }

    public static boolean isOverlapping(Slot slot1, Slot slot2) {
        return isOverlapping(slot1.getStart(), slot1.getEnd(), slot2.getStart(), slot2.getEnd());
    }

    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }

    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[] {
                // Hard constraints
                appointmentConflict(constraintFactory),
                unavailabilityConflict(constraintFactory)
//                teacherConflict(constraintFactory),
//                studentGroupConflict(constraintFactory),
                // Soft constraints
//                teacherRoomStability(constraintFactory),
//                teacherTimeEfficiency(constraintFactory),
//                studentGroupSubjectVariety(constraintFactory)
        };
    }

    Constraint appointmentConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Appointment.class
                    //Joiners.equal(Appointment::getSlot)
                )
                .filter(((appointment, app2) -> {
                    return isOverlapping(appointment, app2);
                }))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Appointment conflict");
    }

    Constraint unavailabilityConflict(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Appointment.class)
                .join(Unavailability.class)
                .filter(((appointment, unavailability) -> {
                    return isOverlapping(appointment, unavailability);
                }))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Unavailability conflict");
    }

    Constraint teacherConflict(ConstraintFactory constraintFactory) {
        // A teacher can teach at most one lesson at the same time.
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getTeacher))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Teacher conflict");
    }

    Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
        // A student can attend at most one lesson at the same time.
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getTimeslot),
                        Joiners.equal(Lesson::getStudentGroup))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Student group conflict");
    }

    Constraint teacherRoomStability(ConstraintFactory constraintFactory) {
        // A teacher prefers to teach in a single room.
        return constraintFactory
                .forEachUniquePair(Lesson.class,
                        Joiners.equal(Lesson::getTeacher))
                .filter((lesson1, lesson2) -> lesson1.getRoom() != lesson2.getRoom())
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("Teacher room stability");
    }

    Constraint teacherTimeEfficiency(ConstraintFactory constraintFactory) {
        // A teacher prefers to teach sequential lessons and dislikes gaps between lessons.
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class, Joiners.equal(Lesson::getTeacher),
                        Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
                .filter((lesson1, lesson2) -> {
                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
                            lesson2.getTimeslot().getStartTime());
                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
                })
                .reward(HardSoftScore.ONE_SOFT)
                .asConstraint("Teacher time efficiency");
    }

    Constraint studentGroupSubjectVariety(ConstraintFactory constraintFactory) {
        // A student group dislikes sequential lessons on the same subject.
        return constraintFactory
                .forEach(Lesson.class)
                .join(Lesson.class,
                        Joiners.equal(Lesson::getSubject),
                        Joiners.equal(Lesson::getStudentGroup),
                        Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
                .filter((lesson1, lesson2) -> {
                    Duration between = Duration.between(lesson1.getTimeslot().getEndTime(),
                            lesson2.getTimeslot().getStartTime());
                    return !between.isNegative() && between.compareTo(Duration.ofMinutes(30)) <= 0;
                })
                .penalize(HardSoftScore.ONE_SOFT)
                .asConstraint("Student group subject variety");
    }

}
