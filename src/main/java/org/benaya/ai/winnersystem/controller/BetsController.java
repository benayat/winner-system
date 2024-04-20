package org.benaya.ai.winnersystem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.MatchChances;
import org.benaya.ai.winnersystem.model.dto.ClientBetDto;
import org.benaya.ai.winnersystem.service.BetsService;
import org.benaya.ai.winnersystem.service.ResultsGeneratorService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
@Slf4j
public class BetsController {
    private final BetsService betsService;
    private final ResultsGeneratorService resultsGeneratorService;

    @PostMapping("/place-bets")
    public void placeBets(@AuthenticationPrincipal(errorOnInvalidType = true) User user, @RequestBody List<ClientBetDto> bets) {
        log.info("Placing bets for user: {}", user.getUsername());
        betsService.placeBets(user.getUsername(), bets);
    }

    @GetMapping("/get-bet-games")
    public List<MatchChances> getBetGamesAndChances(@AuthenticationPrincipal(errorOnInvalidType = true) User user) {
        log.info("Getting bets for user: {}", user.getUsername());
        return resultsGeneratorService.getMatchChancesByUserBets(user.getUsername());
    }
}