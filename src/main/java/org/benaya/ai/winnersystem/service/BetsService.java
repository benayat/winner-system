package org.benaya.ai.winnersystem.service;

import org.benaya.ai.winnersystem.model.Bet;
import org.benaya.ai.winnersystem.model.dto.ClientBetDto;

import java.util.List;

public interface BetsService {
    List<Bet> getAllBetsByUserName(String userName);

    void placeBets(String email, List<ClientBetDto> bets);
    void deleteAllBets();
}
