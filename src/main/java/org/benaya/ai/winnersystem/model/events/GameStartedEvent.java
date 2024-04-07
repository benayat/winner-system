package org.benaya.ai.winnersystem.model.events;

import lombok.*;
import org.benaya.ai.winnersystem.model.Match;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameStartedEvent extends SseEvent{
    private List<Match> allMatches;
}
