package org.benaya.ai.winnersystem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.service.SeasonRunnerService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/season")
@Slf4j
public class SeasonController {
    private final SeasonRunnerService seasonRunnerService;

    @PostMapping("/start")
    public void startSeason() {
        seasonRunnerService.startSeason();
    }

    @GetMapping(value = "/is-active", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean isSeasonActive() {
        log.info("isSeasonActive - {}", seasonRunnerService.isSeasonActive());
        return seasonRunnerService.isSeasonActive();
    }

}
