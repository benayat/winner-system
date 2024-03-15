package org.benaya.ai.winnersystem.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum EventType {
    CHANCES("CHANCES"),
    GOAL_CYCLE_EVENT("GOAL_CYCLE_EVENT");


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
