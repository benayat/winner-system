package org.benaya.ai.winnersystem.controller;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.exception.BetsAreBlockedException;
import org.benaya.ai.winnersystem.model.Bet;
import org.benaya.ai.winnersystem.model.UserProfile;
import org.benaya.ai.winnersystem.service.UserProfileService;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;


@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
public class BetsController {
    private final CacheManager cacheManager;
    private final UserProfileService userProfileService;
    @PostMapping("/bet")
    @PreAuthorize("#bet.betId.userEmail == authentication.principal.username")
    public void bet(@RequestBody Bet bet) {
        Cache blockCache = cacheManager.getCache("betsControllerBlockCache");
        if (blockCache != null && Objects.equals(blockCache.get("blocked", Boolean.class), Boolean.TRUE)) {
            throw new BetsAreBlockedException();
        }
        UserProfile userProfile = userProfileService.getUserProfileByEmail(bet.getBetId().getUserEmail()).orElseThrow();
        if(userProfile.getBalance() < bet.getBetAmount()) {
            throw new IllegalArgumentException("Not enough balance");
        }
        userProfileService.updateBetsAndBalance(bet, userProfile);
    }
}
