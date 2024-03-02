package org.benaya.ai.winnersystem.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class UserProfile {
    @Id
    @Column(nullable = false)
    private String email;
    @Column(nullable = false, unique = true)
    private String userName;
    @Column(nullable = false)
    private String password;
    @ElementCollection
    private List<String> statistics;
}
