package org.benaya.ai.winnersystem.repository;

import org.benaya.ai.winnersystem.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    Optional<UserProfile> findByEmail(String email);
    void deleteByEmail(String email);
}
