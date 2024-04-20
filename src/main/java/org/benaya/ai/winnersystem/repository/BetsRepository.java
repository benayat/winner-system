package org.benaya.ai.winnersystem.repository;

import org.benaya.ai.winnersystem.model.Bet;
import org.benaya.ai.winnersystem.model.BetId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BetsRepository extends JpaRepository<Bet, BetId> {
    List<Bet> getAllByUserProfile_Email(String email);
}