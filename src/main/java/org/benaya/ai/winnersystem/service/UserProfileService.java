package org.benaya.ai.winnersystem.service;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.model.UserProfile;
import org.benaya.ai.winnersystem.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;

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
    public void deleteUserProfile(String email) {
        userProfileRepository.deleteByEmail(email);
    }
}
