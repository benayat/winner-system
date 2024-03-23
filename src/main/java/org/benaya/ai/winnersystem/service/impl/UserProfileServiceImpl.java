package org.benaya.ai.winnersystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.model.Bet;
import org.benaya.ai.winnersystem.model.UserProfile;
import org.benaya.ai.winnersystem.repository.BetRepository;
import org.benaya.ai.winnersystem.repository.UserProfileRepository;
import org.benaya.ai.winnersystem.service.UserProfileService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final BetRepository betRepository;

    public List<Bet> getAllBetsByUserName(String userName) {
        return betRepository.getAllByUserProfile_UserName(userName);
    }
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
    public UserProfile updateBetsAndBalance(Bet bet, UserProfile userProfile) {
        userProfile.getBets().add(bet);
        userProfile.setBalance(userProfile.getBalance() - bet.getBetAmount());
        return userProfileRepository.save(userProfile);
    }
    public void deleteUserProfile(String email) {
        userProfileRepository.deleteByEmail(email);
    }



}
