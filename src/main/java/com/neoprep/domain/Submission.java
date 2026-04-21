package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserProfile user;

    @ManyToOne
    private DayPlan dayPlan;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SubmissionType submissionType;

    @Column(length = 10000)
    private String codeText;

    @Lob
    private String imageBase64;

    @Column(nullable = false)
    private String problemName;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }
    public DayPlan getDayPlan() { return dayPlan; }
    public void setDayPlan(DayPlan dayPlan) { this.dayPlan = dayPlan; }
    public SubmissionType getSubmissionType() { return submissionType; }
    public void setSubmissionType(SubmissionType submissionType) { this.submissionType = submissionType; }
    public String getCodeText() { return codeText; }
    public void setCodeText(String codeText) { this.codeText = codeText; }
    public String getImageBase64() { return imageBase64; }
    public void setImageBase64(String imageBase64) { this.imageBase64 = imageBase64; }
    public String getProblemName() { return problemName; }
    public void setProblemName(String problemName) { this.problemName = problemName; }
    public Instant getCreatedAt() { return createdAt; }
}
