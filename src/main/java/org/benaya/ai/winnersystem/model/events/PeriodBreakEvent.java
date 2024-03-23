package org.benaya.ai.winnersystem.model.events;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeriodBreakEvent extends SseEvent{
    boolean blockBets;
}
