package com.neoprep.domain;

import jakarta.persistence.*;

@Entity
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    private Submission submission;

    @Column(nullable = false)
    private String timeComplexity;

    @Column(nullable = false)
    private String spaceComplexity;

    @Column(nullable = false, length = 4000)
    private String bugs;

    @Column(nullable = false, length = 8000)
    private String optimalSolution;

    @Column(nullable = false, length = 2000)
    private String algorithmPattern;

    public Long getId() { return id; }
    public Submission getSubmission() { return submission; }
    public void setSubmission(Submission submission) { this.submission = submission; }
    public String getTimeComplexity() { return timeComplexity; }
    public void setTimeComplexity(String timeComplexity) { this.timeComplexity = timeComplexity; }
    public String getSpaceComplexity() { return spaceComplexity; }
    public void setSpaceComplexity(String spaceComplexity) { this.spaceComplexity = spaceComplexity; }
    public String getBugs() { return bugs; }
    public void setBugs(String bugs) { this.bugs = bugs; }
    public String getOptimalSolution() { return optimalSolution; }
    public void setOptimalSolution(String optimalSolution) { this.optimalSolution = optimalSolution; }
    public String getAlgorithmPattern() { return algorithmPattern; }
    public void setAlgorithmPattern(String algorithmPattern) { this.algorithmPattern = algorithmPattern; }
}
