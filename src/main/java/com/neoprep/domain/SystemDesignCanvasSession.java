package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class SystemDesignCanvasSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserProfile user;

    @Lob
    @Column(nullable = false)
    private String canvasJson;

    @Column(nullable = false, length = 5000)
    private String jarvisFeedback;

    @Column(nullable = false)
    private boolean beta;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }
    public String getCanvasJson() { return canvasJson; }
    public void setCanvasJson(String canvasJson) { this.canvasJson = canvasJson; }
    public String getJarvisFeedback() { return jarvisFeedback; }
    public void setJarvisFeedback(String jarvisFeedback) { this.jarvisFeedback = jarvisFeedback; }
    public boolean isBeta() { return beta; }
    public void setBeta(boolean beta) { this.beta = beta; }
    public Instant getCreatedAt() { return createdAt; }
}
