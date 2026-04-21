package com.neoprep.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private UserProfile user;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, length = 4000)
    private String linkedInTemplate;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public Long getId() { return id; }
    public UserProfile getUser() { return user; }
    public void setUser(UserProfile user) { this.user = user; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLinkedInTemplate() { return linkedInTemplate; }
    public void setLinkedInTemplate(String linkedInTemplate) { this.linkedInTemplate = linkedInTemplate; }
    public Instant getCreatedAt() { return createdAt; }
}
