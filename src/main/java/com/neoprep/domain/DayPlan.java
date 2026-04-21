package com.neoprep.domain;

import jakarta.persistence.*;

@Entity
public class DayPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Roadmap roadmap;

    @Column(nullable = false)
    private Integer dayNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FocusArea focusArea;

    @Column(nullable = false)
    private String primaryTopic;

    @Column(nullable = false, length = 1000)
    private String actionableTask;

    private String leetcodePattern;

    private boolean completed;

    public Long getId() { return id; }
    public Roadmap getRoadmap() { return roadmap; }
    public void setRoadmap(Roadmap roadmap) { this.roadmap = roadmap; }
    public Integer getDayNumber() { return dayNumber; }
    public void setDayNumber(Integer dayNumber) { this.dayNumber = dayNumber; }
    public FocusArea getFocusArea() { return focusArea; }
    public void setFocusArea(FocusArea focusArea) { this.focusArea = focusArea; }
    public String getPrimaryTopic() { return primaryTopic; }
    public void setPrimaryTopic(String primaryTopic) { this.primaryTopic = primaryTopic; }
    public String getActionableTask() { return actionableTask; }
    public void setActionableTask(String actionableTask) { this.actionableTask = actionableTask; }
    public String getLeetcodePattern() { return leetcodePattern; }
    public void setLeetcodePattern(String leetcodePattern) { this.leetcodePattern = leetcodePattern; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
