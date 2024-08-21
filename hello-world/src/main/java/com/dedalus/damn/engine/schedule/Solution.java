package com.dedalus.damn.engine.schedule;

import com.dedalus.damn.engine.schedule.solver.Schedule;
import lombok.*;
import org.optaplanner.core.api.score.ScoreExplanation;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Solution {

    Schedule schedule;
    ScoreExplanation<Schedule, HardSoftScore> scoreExplanation;

}
