package com.dedalus.damn.engine.schedule.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class TimeSlot {
    private LocalDateTime start;
    private LocalDateTime end;

    public TimeSlot(Date start, Date end) {
        this.start = toLocal(start);
        this.end = toLocal(end);
    }

//    public static TimeSlot fromDate(Date start, Date end) {
//        return new TimeSlot(toLocal(start), toLocal(end));
//    }

    private LocalDateTime toLocal(Date d) {
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
