package org.benaya.ai.winnersystem.model.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.benaya.ai.winnersystem.model.MatchChances;

import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
public class ChancesEvent extends SseEvent {
    private List<MatchChances> matchChances;
}
