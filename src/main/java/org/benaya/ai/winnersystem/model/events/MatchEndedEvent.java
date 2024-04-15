package org.benaya.ai.winnersystem.model.events;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchEndedEvent extends SseEvent {
    public MatchEndedEvent() {
        super(EventType.MATCH_ENDED_EVENT);    }
}
