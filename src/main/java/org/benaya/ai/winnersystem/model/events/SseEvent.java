package org.benaya.ai.winnersystem.model.events;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class SseEvent implements Serializable {
    private EventType eventType;
}
