package org.benaya.ai.winnersystem.service;

import org.benaya.ai.winnersystem.model.UserProfile;

public interface UserProfileService {
    UserProfile createUserProfile(UserProfile userProfile);
    UserProfile updateEmail(String email, String newEmail);
    UserProfile updateUserName(String email, String newUserName);
    void deleteUserProfile(String email);
}
