package org.benaya.ai.winnersystem.model.events;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
public class GameStartedEvent extends SseEvent{
    public GameStartedEvent() {
        super(EventType.GAME_STARTED_EVENT);
    }
}
