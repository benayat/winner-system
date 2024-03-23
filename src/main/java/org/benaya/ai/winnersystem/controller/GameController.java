package org.benaya.ai.winnersystem.controller;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.service.GameRunnerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameRunnerService gameRunnerService;
    @PostMapping("/start")
    public void startGame() {
        gameRunnerService.startGame();
    }
}
