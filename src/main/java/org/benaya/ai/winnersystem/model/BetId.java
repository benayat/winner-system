package org.benaya.ai.winnersystem.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class BetId implements Serializable {
    private String userName;
    private Match match;
}
