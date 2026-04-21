package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class QuizAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserProfile user;

    @ManyToOne
    private DayPlan dayPlan;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    private int maxScore;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }
    public DayPlan getDayPlan() { return dayPlan; }
    public void setDayPlan(DayPlan dayPlan) { this.dayPlan = dayPlan; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public int getMaxScore() { return maxScore; }
    public void setMaxScore(int maxScore) { this.maxScore = maxScore; }
    public Instant getCreatedAt() { return createdAt; }
}
