package org.benaya.ai.winnersystem.controller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benaya.ai.winnersystem.model.UserProfile;
import org.benaya.ai.winnersystem.service.UserProfileService;
import org.benaya.ai.winnersystem.validate.annotations.ValidPassword;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserProfileService userProfileServiceImpl;
    @GetMapping("/")
    public String getUserProfile(@AuthenticationPrincipal User user) {
        log.info("inside getUserProfile, returning {}", user.getUsername());
        return user.getUsername();
    }
    @GetMapping("/balance")
    public int getBalance(@AuthenticationPrincipal User user) {
        return userProfileServiceImpl.getBalanceForUser(user.getUsername());
    }
    @GetMapping(value = "/logged-in", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean isLoggedIn(@RequestParam String email) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName().equals(email);
    }
    @PostMapping(value = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public UserProfile createUser(@RequestBody UserProfile userprofile) {
        log.info("inside createUser");
        return userProfileServiceImpl.createUserProfile(userprofile);
    }
    @PutMapping("/update-email")
    @PreAuthorize("#email == #user.username")
    public UserProfile updateEmail(@AuthenticationPrincipal User user, @RequestParam String email, @RequestParam("new-email") String newEmail) {
        return userProfileServiceImpl.updateEmail(email, newEmail);
    }

    @PutMapping("/update-username")
    @PreAuthorize("#email == #user.username")
    public String updateUsername(@AuthenticationPrincipal User user, @RequestParam String email, @RequestParam("new-username") String newUserName) {
        return userProfileServiceImpl.updateUserName(user.getUsername(), newUserName).getUserName();
    }

    @PutMapping("/update-password")
    @PreAuthorize("#email == #user.username")
    public void updatePassword(@AuthenticationPrincipal User user, @RequestParam String email, @RequestParam("new-password") @ValidPassword String newPassword) {
        userProfileServiceImpl.updatePassword(user.getUsername(), newPassword);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("#email == #user.username")
    public void deleteUser(@AuthenticationPrincipal User user, @RequestParam String email) {
        userProfileServiceImpl.deleteUserProfile(email);
    }
}