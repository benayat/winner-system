package org.benaya.ai.winnersystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.exception.BetsAreBlockedException;
import org.benaya.ai.winnersystem.model.Bet;
import org.benaya.ai.winnersystem.model.dto.ClientBet;
import org.benaya.ai.winnersystem.repository.BetsRepository;
import org.benaya.ai.winnersystem.service.BetsService;
import org.benaya.ai.winnersystem.service.UserProfileService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BetsServiceImpl implements BetsService {
    private final BetsRepository betsRepository;
    private final CacheManager cacheManager;
    private final UserProfileService userProfileService;
    public List<Bet> getAllBetsByUserName(String userName) {
        return betsRepository.getAllByUserProfile_UserName(userName);
    }
    public void placeBets(String email, List<ClientBet> bets) {
        Cache blockCache = cacheManager.getCache("betsControllerBlockCache");
        if (blockCache != null && Objects.equals(blockCache.get("blocked", Boolean.class), Boolean.TRUE)) {
            throw new BetsAreBlockedException();
        }
        List<Bet> betsToSend = bets.stream().map(bet -> new Bet(email, bet.getTeam1Name(), bet.getTeam2Name(), bet.getExpectedWinner(), bet.getAmount())).toList();
        userProfileService.placeBetsForPeriod(betsToSend, email);
    }

    public void deleteAllBets() {
        betsRepository.deleteAll();
    }
}
