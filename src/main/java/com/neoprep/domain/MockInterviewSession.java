package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class MockInterviewSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserProfile user;

    @Column(nullable = false)
    private String mode;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false, length = 4000)
    private String prompt;

    @Column(nullable = false, length = 4000)
    private String followUpGuidance;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }
    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }
    public String getFollowUpGuidance() { return followUpGuidance; }
    public void setFollowUpGuidance(String followUpGuidance) { this.followUpGuidance = followUpGuidance; }
    public Instant getCreatedAt() { return createdAt; }
}
