package org.benaya.ai.winnersystem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.dto.ClientBetDto;
import org.benaya.ai.winnersystem.service.BetsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
@Slf4j
public class BetsController {
    private final BetsService betsService;
    @PostMapping("/place-bets")
    public void placeBets(@AuthenticationPrincipal(errorOnInvalidType = true) User user, @RequestBody List<ClientBetDto> bets) {
        log.info("Placing bets for user: {}", user.getUsername());
        betsService.placeBets(user.getUsername(), bets);
    }
}
