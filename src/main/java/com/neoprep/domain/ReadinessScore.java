package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class ReadinessScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserProfile user;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private int scorePercent;

    @Column(nullable = false, length = 4000)
    private String topGaps;

    @Column(nullable = false, length = 4000)
    private String nextActions;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public int getScorePercent() { return scorePercent; }
    public void setScorePercent(int scorePercent) { this.scorePercent = scorePercent; }
    public String getTopGaps() { return topGaps; }
    public void setTopGaps(String topGaps) { this.topGaps = topGaps; }
    public String getNextActions() { return nextActions; }
    public void setNextActions(String nextActions) { this.nextActions = nextActions; }
    public Instant getCreatedAt() { return createdAt; }
}
