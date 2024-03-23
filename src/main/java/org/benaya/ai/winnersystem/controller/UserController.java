package org.benaya.ai.winnersystem.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.UserProfile;
import org.benaya.ai.winnersystem.service.UserProfileService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserProfileService userProfileServiceImpl;

    @GetMapping("/")
    public String getUserProfile(@AuthenticationPrincipal String email) {
        return email;
    }

    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfile createUser(@RequestBody UserProfile userprofile) {
        log.info("inside createUser");
        return userProfileServiceImpl.createUserProfile(userprofile);
    }

    @PutMapping("/update-email")
    @PreAuthorize("#email == authentication.principal.username")
    public UserProfile updateEmail(@AuthenticationPrincipal String email, @RequestParam("new-email") String newEmail) {
        return userProfileServiceImpl.updateEmail(email, newEmail);
    }
    @PutMapping("/update-username")
    @PreAuthorize("#email == authentication.principal.username")
    public UserProfile updateUsername(@AuthenticationPrincipal String email, @RequestParam("new-username") String newUserName) {
        return userProfileServiceImpl.updateUserName(email, newUserName);
    }
    @DeleteMapping("/delete")
    @PreAuthorize("#email == authentication.principal.username")
    public void deleteUser(@AuthenticationPrincipal String email) {
        userProfileServiceImpl.deleteUserProfile(email);
    }
}
