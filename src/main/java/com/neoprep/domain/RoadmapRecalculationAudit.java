package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class RoadmapRecalculationAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Roadmap roadmap;

    @Column(nullable = false, length = 4000)
    private String reasonSummary;

    @Column(nullable = false, length = 6000)
    private String reasonMetadata;

    @Column(nullable = false)
    private int changedDays;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public Roadmap getRoadmap() { return roadmap; }
    public void setRoadmap(Roadmap roadmap) { this.roadmap = roadmap; }
    public String getReasonSummary() { return reasonSummary; }
    public void setReasonSummary(String reasonSummary) { this.reasonSummary = reasonSummary; }
    public String getReasonMetadata() { return reasonMetadata; }
    public void setReasonMetadata(String reasonMetadata) { this.reasonMetadata = reasonMetadata; }
    public int getChangedDays() { return changedDays; }
    public void setChangedDays(int changedDays) { this.changedDays = changedDays; }
    public Instant getCreatedAt() { return createdAt; }
}
