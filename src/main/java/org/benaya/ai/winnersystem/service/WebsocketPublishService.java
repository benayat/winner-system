package org.benaya.ai.winnersystem.service;

import org.benaya.ai.winnersystem.model.Match;
import org.benaya.ai.winnersystem.model.MatchResults;

import java.util.Map;

public interface WebsocketPublishService {
    void publishGoalCycleResultsToUI(Map<Match, MatchResults> tempResultsMap);
}
