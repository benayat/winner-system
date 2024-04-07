package org.benaya.ai.winnersystem.listeners;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.factory.SseFactory;
import org.benaya.ai.winnersystem.model.BetId;
import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchChances;
import org.benaya.ai.winnersystem.model.events.*;
import org.benaya.ai.winnersystem.service.BetsService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseEventListener {

    private final SseFactory sseFactory;
    private final BetsService betsService;

    @EventListener(SseEvent.class)
    @Async
    public void onSseEvent(SseEvent sseEvent){
        switch(sseEvent.getEventType()){
            case GAME_STARTED_EVENT -> onGameStartedEvent((GameStartedEvent) sseEvent);
            case CHANCES_EVENT -> onChancesEvent((ChancesEvent) sseEvent);
            case GOAL_CYCLE_EVENT -> onGoalCycleEvent((GoalCycleEvent) sseEvent);
            case PERIOD_BREAK_EVENT -> onPeriodBreakEvent((PeriodBreakEvent) sseEvent);
        }
    }
    public void onChancesEvent(ChancesEvent chancesEvent) {
        sseFactory.getSecureEmitters().forEach((userName, emitter) -> {
            List<Match> matchesToSend = betsService.getAllBetsByUserName(userName).stream().map(bet -> {
                BetId betId = bet.getBetId();
                return new Match(betId.getTeam1Name(), betId.getTeam2Name());
            }).toList();
            try {
                List<MatchChances> matchChancesToSend = chancesEvent.getMatchChances().stream()
                        .filter(matchChances -> matchesToSend.contains(createMatchObjectFromTwoTeams(matchChances.getTeam1Name(), matchChances.getTeam2Name()))).toList();
                chancesEvent.setMatchChances(matchChancesToSend);
                emitter.send(chancesEvent);
                log.info("Sent chances event to user: " + userName + " with " + matchChancesToSend.size() + " matches");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void onGoalCycleEvent(GoalCycleEvent goalCycleEvent) {
        log.info("Sent goal cycle event to all users");
        sseFactory.getSimpleEmitters().parallelStream().forEach(emitter -> {
            try {
                emitter.send(goalCycleEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void onGameStartedEvent(GameStartedEvent gameStartedEvent) {
        log.info("Sending gameStartedEvent to all users");
        sseFactory.getSimpleEmitters().parallelStream().forEach(emitter -> {
            try {
                emitter.send(gameStartedEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void onPeriodBreakEvent(PeriodBreakEvent periodBreakEvent) {
        log.info("Sending periodBreakEvent to all users");
        sseFactory.getSecureEmitters().forEach((userName, emitter)-> {
            try {
                emitter.send(periodBreakEvent);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private static Match createMatchObjectFromTwoTeams(String team1, String team2) {
        return new Match(team1, team2);
    }
}
