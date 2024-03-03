package org.benaya.ai.winnersystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.benaya.ai.winnersystem.validate.annotations.ValidPassword;

import java.util.List;

@Entity
@Data
public class UserProfile {
    @Id
    @Column(nullable = false)
    @Email
    private String email;
    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false)
    @ValidPassword
    private String password;
    @ElementCollection
    private List<String> statistics;
}
