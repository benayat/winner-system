package org.benaya.ai.winnersystem.service;

import org.benaya.ai.winnersystem.model.Bet;
import org.benaya.ai.winnersystem.model.UserProfile;

import java.util.List;
import java.util.Optional;

public interface UserProfileService {
    List<Bet> getAllBetsByUserName(String userName);
    Optional<UserProfile> getUserProfileByEmail(String email);
    UserProfile createUserProfile(UserProfile userProfile);
    UserProfile updateEmail(String email, String newEmail);
    UserProfile updateUserName(String email, String newUserName);
    void deleteUserProfile(String email);
    UserProfile updateBetsAndBalance(Bet bet, UserProfile userProfile);
}
