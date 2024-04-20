package org.benaya.ai.winnersystem.model.events;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum Units {
    SECONDS("SECONDS"),
    MINUTES("MINUTES");
    private final String value;
    private static final Map<String, Units> lookup = new HashMap<>();

    static {
        for (Units event : Units.values()) {
            lookup.put(event.getValue(), event);
        }
    }

    public static Units get(String value) {
        return lookup.get(value.toUpperCase());
    }
}