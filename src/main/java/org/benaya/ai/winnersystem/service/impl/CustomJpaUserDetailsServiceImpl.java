package org.benaya.ai.winnersystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.benaya.ai.winnersystem.model.UserProfile;
import org.benaya.ai.winnersystem.service.UserProfileService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomJpaUserDetailsServiceImpl implements UserDetailsService {
    private final UserProfileService userProfileService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserProfile userProfile = userProfileService.getUserProfileByEmail(email).orElseThrow();
        return new org.springframework.security.core.userdetails
                .User(userProfile.getEmail(), userProfile.getPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
