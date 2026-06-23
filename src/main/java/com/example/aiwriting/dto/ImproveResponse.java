package com.example.aiwriting.dto;

import java.time.LocalDateTime;

public class ImproveResponse {
    private Long id;
    private String original;
    private String formal;
    private String natural;
    private String business;
    private LocalDateTime createdAt;

    public ImproveResponse(Long id, String original, String formal, String natural, String business, LocalDateTime createdAt) {
        this.id = id;
        this.original = original;
        this.formal = formal;
        this.natural = natural;
        this.business = business;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getOriginal() { return original; }
    public String getFormal() { return formal; }
    public String getNatural() { return natural; }
    public String getBusiness() { return business; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
