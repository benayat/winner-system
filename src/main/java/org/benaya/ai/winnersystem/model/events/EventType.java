package org.benaya.ai.winnersystem.model.events;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
@AllArgsConstructor
@Getter
public enum EventType {
    GOAL_CYCLE_EVENT("GOAL_CYCLE_EVENT"),
    TIMER_EVENT("TIMER_EVENT"),
    PERIOD_TIMER_EVENT("PERIOD_TIMER_EVENT"),
    PERIOD_BREAK_EVENT("PERIOD_BREAK_EVENT"),
    MATCH_STARTED_EVENT("MATCH_STARTED_EVENT"),
    MATCH_ENDED_EVENT("MATCH_ENDED_EVENT");
    private final String value;
    private static final Map<String, EventType> lookup = new HashMap<>();
    static {
        for (EventType event : EventType.values()) {
            lookup.put(event.getValue(), event);
        }
    }
    public static EventType get(String value) {
        return lookup.get(value.toUpperCase());
    }
}