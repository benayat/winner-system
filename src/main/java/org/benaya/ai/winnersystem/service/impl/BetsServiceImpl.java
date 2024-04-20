package org.benaya.ai.winnersystem.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.exception.BetsAreBlockedException;
import org.benaya.ai.winnersystem.model.Bet;
import org.benaya.ai.winnersystem.model.UserProfile;
import org.benaya.ai.winnersystem.model.dto.ClientBetDto;
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

    public List<Bet> getAllBetsByUserName(String email) {
        return betsRepository.getAllByUserProfile_Email(email);
    }

    @Transactional
    public void placeBets(String email, List<ClientBetDto> bets) {
        Cache blockCache = cacheManager.getCache("betsControllerBlockCache");
        if (blockCache != null && Objects.equals(blockCache.get("blocked", Boolean.class), Boolean.TRUE)) {
            throw new BetsAreBlockedException();
        }
        UserProfile userProfile = userProfileService.getUserProfileByEmail(email).orElseThrow();
        List<Bet> betsToSend = bets.stream().map(bet -> new Bet(email, bet.getTeam1Name(), bet.getTeam2Name(), userProfile, bet.getExpectedWinner(), bet.getAmount())).toList();
        int betsAmount = betsToSend.stream().mapToInt(Bet::getBetAmount).sum();
        userProfileService.handleSideEffectsForUserBets(betsAmount, userProfile);
        betsRepository.saveAll(betsToSend);
    }

    public void deleteAllBets() {
        betsRepository.deleteAll();
    }
}