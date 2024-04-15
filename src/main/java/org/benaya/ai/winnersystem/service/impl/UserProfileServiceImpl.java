package org.benaya.ai.winnersystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.exception.LowBalanceException;
import org.benaya.ai.winnersystem.model.Bet;
import org.benaya.ai.winnersystem.model.UserProfile;
import org.benaya.ai.winnersystem.model.Winner;
import org.benaya.ai.winnersystem.repository.UserProfileRepository;
import org.benaya.ai.winnersystem.service.UserProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;

    public Optional<UserProfile> getUserProfileByEmail(String email) {
        return userProfileRepository.findByEmail(email);
    }

    public UserProfile createUserProfile(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    public UserProfile updateEmail(String email, String newEmail) {
        UserProfile currentSavedUser = userProfileRepository.findByEmail(email).orElseThrow();
        currentSavedUser.setEmail(newEmail);
        return userProfileRepository.save(currentSavedUser);
    }

    public UserProfile updateUserName(String email, String newUserName) {
        UserProfile currentSavedUser = userProfileRepository.findByEmail(email).orElseThrow();
        currentSavedUser.setUserName(newUserName);
        return userProfileRepository.save(currentSavedUser);
    }


    public void handleSideEffectsForUserBets(int betsAmount, UserProfile userProfile) {
        if (!userProfile.getBets().isEmpty()) {
            int amountToRestore = userProfile.getBets().stream().mapToInt(Bet::getBetAmount).sum();
            userProfile.setBalance(userProfile.getBalance() + amountToRestore);
        }
        if (userProfile.getBalance() < betsAmount) {
            throw new LowBalanceException();
        }
        userProfile.setBalance(userProfile.getBalance() - betsAmount);
        userProfileRepository.save(userProfile);
    }

    public void deleteUserProfile(String email) {
        userProfileRepository.deleteByEmail(email);
    }

    public List<UserProfile> getAllByBets_BetId_Team1NameAndBets_BetId_Team2NameAndWinnerName(String team1Name, String team2Name, Winner winner) {
        return userProfileRepository.getAllByBets_BetId_Team1NameAndBets_BetId_Team2NameAndBets_Winner(team1Name, team2Name, winner);
    }

    public void saveAll(List<UserProfile> userProfiles) {
        userProfileRepository.saveAll(userProfiles);
    }
    public int getBalanceForUser(String email) {
        return userProfileRepository.findByEmail(email).orElseThrow().getBalance();
    }
}
