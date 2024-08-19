package com.dedalus.damn.engine.schedule.factory;

import com.dedalus.damn.engine.schedule.domain.Resource;
import com.dedalus.damn.engine.schedule.domain.TimeSlot;

import java.util.List;

public class FactoryResource {

    static long id = 0;

    public static Resource create(String name, List<TimeSlot> unavailabilities) {
        return new Resource(id++, name, unavailabilities);
    }

}
