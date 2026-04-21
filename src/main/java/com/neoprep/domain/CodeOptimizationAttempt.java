package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class CodeOptimizationAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserProfile user;

    @Column(nullable = false)
    private String problemName;

    @Lob
    private String codeText;

    @Lob
    private String imageBase64;

    @Column(nullable = false, length = 4000)
    private String memoryFeedback;

    @Column(nullable = false, length = 4000)
    private String complexityFeedback;

    @Column(nullable = false, length = 4000)
    private String correctnessFeedback;

    @Column(nullable = false, length = 4000)
    private String readabilityFeedback;

    @Column(nullable = false, length = 12000)
    private String lineByLineFeedback;

    @Column(nullable = false, length = 2000)
    private String improvementDelta;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }
    public String getProblemName() { return problemName; }
    public void setProblemName(String problemName) { this.problemName = problemName; }
    public String getCodeText() { return codeText; }
    public void setCodeText(String codeText) { this.codeText = codeText; }
    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
    public String getMemoryFeedback() { return memoryFeedback; }
    public void setMemoryFeedback(String memoryFeedback) { this.memoryFeedback = memoryFeedback; }
    public String getComplexityFeedback() { return complexityFeedback; }
    public void setComplexityFeedback(String complexityFeedback) { this.complexityFeedback = complexityFeedback; }
    public String getCorrectnessFeedback() { return correctnessFeedback; }
    public void setCorrectnessFeedback(String correctnessFeedback) { this.correctnessFeedback = correctnessFeedback; }
    public String getReadabilityFeedback() { return readabilityFeedback; }
    public void setReadabilityFeedback(String readabilityFeedback) { this.readabilityFeedback = readabilityFeedback; }
    public String getLineByLineFeedback() { return lineByLineFeedback; }
    public void setLineByLineFeedback(String lineByLineFeedback) { this.lineByLineFeedback = lineByLineFeedback; }
    public String getImprovementDelta() { return improvementDelta; }
    public void setImprovementDelta(String improvementDelta) { this.improvementDelta = improvementDelta; }
    public Instant getCreatedAt() { return createdAt; }
}
