package org.benaya.ai.winnersystem.repository;
import org.benaya.ai.winnersystem.model.UserProfile;
import org.benaya.ai.winnersystem.model.Winner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByEmail(String email);
    void deleteByEmail(String email);
    List<UserProfile> getAllByBets_BetId_Team1NameAndBets_BetId_Team2NameAndBets_Winner(String team1Name, String team2Name, Winner winner);
}