package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class CompanyPattern {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String company;

    @Column(nullable = false)
    private String tier;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, length = 3000)
    private String patternDetail;

    @Column(nullable = false)
    private Integer freshnessScore;

    @Column(nullable = false)
    private Double sourceConfidence;

    @Column(nullable = false)
    private String source;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getTier() { return tier; }
    public void setTier(String tier) { this.tier = tier; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getPatternDetail() { return patternDetail; }
    public void setPatternDetail(String patternDetail) { this.patternDetail = patternDetail; }
    public Integer getFreshnessScore() { return freshnessScore; }
    public void setFreshnessScore(Integer freshnessScore) { this.freshnessScore = freshnessScore; }
    public Double getSourceConfidence() { return sourceConfidence; }
    public void setSourceConfidence(Double sourceConfidence) { this.sourceConfidence = sourceConfidence; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public Instant getCreatedAt() { return createdAt; }
}
