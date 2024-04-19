package org.benaya.ai.winnersystem.service;
import org.benaya.ai.winnersystem.model.UserProfile;
import org.benaya.ai.winnersystem.model.Winner;

import java.util.List;
import java.util.Optional;
public interface UserProfileService {
    Optional<UserProfile> getUserProfileByEmail(String email);
    UserProfile createUserProfile(UserProfile userProfile);
    UserProfile updateEmail(String email, String newEmail);
    UserProfile updateUserName(String email, String newUserName);
    void updatePassword(String email, String newPassword);
    void deleteUserProfile(String email);
    void handleSideEffectsForUserBets(int betsAmount, UserProfile userProfile);
    List<UserProfile> getAllByBets_BetId_Team1NameAndBets_BetId_Team2NameAndWinnerName(String team1Name, String team2Name, Winner winner);
    void saveAll(List<UserProfile> userProfiles);
    int getBalanceForUser(String email);
}