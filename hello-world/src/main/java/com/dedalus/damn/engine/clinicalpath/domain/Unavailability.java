package com.dedalus.damn.engine.clinicalpath.domain;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Unavailability {

    private LocalDateTime start;
    private LocalDateTime end;

}
