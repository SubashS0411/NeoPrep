package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class WhiteboardAnalysisSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserProfile user;

    @Lob
    private String imageBase64;

    @Column(nullable = false, length = 6000)
    private String analysis;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }
    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
    public String getAnalysis() { return analysis; }
    public void setAnalysis(String analysis) { this.analysis = analysis; }
    public Instant getCreatedAt() { return createdAt; }
}
