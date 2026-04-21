package com.neoprep.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String targetCompanyTier;

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTargetCompanyTier() { return targetCompanyTier; }
    public void setTargetCompanyTier(String targetCompanyTier) { this.targetCompanyTier = targetCompanyTier; }
}
