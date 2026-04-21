package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDate;

@Entity
public class StandupLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserProfile user;

    @Column(nullable = false)
    private LocalDate standupDate;

    @Column(nullable = false, length = 4000)
    private String summary;

    @Column(nullable = false, length = 2000)
    private String marketWhy;

    @Column(length = 2000)
    private String carryOver;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }
    public LocalDate getStandupDate() { return standupDate; }
    public void setStandupDate(LocalDate standupDate) { this.standupDate = standupDate; }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getMarketWhy() { return marketWhy; }
    public void setMarketWhy(String marketWhy) { this.marketWhy = marketWhy; }
    public String getCarryOver() { return carryOver; }
    public void setCarryOver(String carryOver) { this.carryOver = carryOver; }
    public Instant getCreatedAt() { return createdAt; }
}
