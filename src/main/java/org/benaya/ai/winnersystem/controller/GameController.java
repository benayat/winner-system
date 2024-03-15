package org.benaya.ai.winnersystem.controller;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.service.ResultsGeneratorService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/game")
public class GameController {
    private final ResultsGeneratorService resultsGeneratorService;
    @PutMapping("/start")
    public void startGame() {
        resultsGeneratorService.startSeason();
    }

}
