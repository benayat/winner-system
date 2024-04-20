package org.benaya.ai.winnersystem.model.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public abstract class SseEvent implements Serializable {
    @JsonProperty("event")
    private EventType eventType;
}