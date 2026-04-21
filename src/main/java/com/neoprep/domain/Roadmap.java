package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Roadmap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserProfile user;

    @Column(nullable = false)
    private String roadmapName;

    private String targetCompanyTier;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayNumber ASC")
    private List<DayPlan> days = new ArrayList<>();

    public Long getId() { return id; }
    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }
    public String getRoadmapName() { return roadmapName; }
    public void setRoadmapName(String roadmapName) { this.roadmapName = roadmapName; }
    public String getTargetCompanyTier() { return targetCompanyTier; }
    public void setTargetCompanyTier(String targetCompanyTier) { this.targetCompanyTier = targetCompanyTier; }
    public Instant getCreatedAt() { return createdAt; }
    public List<DayPlan> getDays() { return days; }
}
