package org.benaya.ai.winnersystem.model.dto;

import lombok.Data;
import org.benaya.ai.winnersystem.model.Winner;

@Data
public class ClientBetDto {
    private String team1Name;
    private String team2Name;
    private Winner expectedWinner;
    private int amount;
}
