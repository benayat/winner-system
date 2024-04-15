package org.benaya.ai.winnersystem.model.events;

import lombok.Getter;
import lombok.Setter;

import static org.benaya.ai.winnersystem.constant.MatchConstants.BREAK_TIME_IN_SECONDS;

@Getter
@Setter
public class TimerEvent extends SseEvent {
    private int value;
    private Units units;
    private TimerType timerType;
    String message;

    public TimerEvent(int value, Units units) {
        super(EventType.TIMER_EVENT);
        this.value = value;
        this.units = units;
        this.timerType = units.equals(Units.MINUTES) ? TimerType.MATCH : TimerType.BETS;
        this.message = getTimerType().getValue() + ":" + (getTimerType().equals(TimerType.MATCH)?value:BREAK_TIME_IN_SECONDS-value) + " " + units;
    }
}
