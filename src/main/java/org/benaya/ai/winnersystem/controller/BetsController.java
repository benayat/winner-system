package org.benaya.ai.winnersystem.controller;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.model.dto.ClientBet;
import org.benaya.ai.winnersystem.service.BetsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/bets")
@RequiredArgsConstructor
public class BetsController {
    private final BetsService betsService;
    @PostMapping("/place-bets")
    public void placeBets(@AuthenticationPrincipal String email, @RequestBody List<ClientBet> bets) {
        betsService.placeBets(email, bets);
    }

}
