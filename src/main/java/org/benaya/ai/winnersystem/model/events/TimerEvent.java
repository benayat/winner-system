package org.benaya.ai.winnersystem.model.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimerEvent extends SseEvent{
    private int value;
    private Units units;

    public TimerEvent(int value, Units units) {
        super(EventType.TIMER_EVENT);
        this.value = value;
        this.units = units;
    }
}
