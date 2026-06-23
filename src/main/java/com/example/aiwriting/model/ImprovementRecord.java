package com.example.aiwriting.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "improvement_records")
public class ImprovementRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String originalText;

    @Column(columnDefinition = "TEXT")
    private String formalText;

    @Column(columnDefinition = "TEXT")
    private String naturalText;

    @Column(columnDefinition = "TEXT")
    private String businessText;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOriginalText() { return originalText; }
    public void setOriginalText(String originalText) { this.originalText = originalText; }

    public String getFormalText() { return formalText; }
    public void setFormalText(String formalText) { this.formalText = formalText; }

    public String getNaturalText() { return naturalText; }
    public void setNaturalText(String naturalText) { this.naturalText = naturalText; }

    public String getBusinessText() { return businessText; }
    public void setBusinessText(String businessText) { this.businessText = businessText; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
