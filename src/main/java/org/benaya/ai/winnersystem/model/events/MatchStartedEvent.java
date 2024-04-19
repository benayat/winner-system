package org.benaya.ai.winnersystem.model.events;
import lombok.*;
import org.benaya.ai.winnersystem.model.Match;

import java.util.List;
@Getter
@Setter
public class MatchStartedEvent extends SseEvent{
    private List<Match> allMatches;
    public MatchStartedEvent(List<Match> allMatches) {
        super(EventType.MATCH_STARTED_EVENT);
        this.allMatches = allMatches;
    }
}