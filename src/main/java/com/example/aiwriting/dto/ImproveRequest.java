package com.example.aiwriting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ImproveRequest {

    @NotBlank(message = "Text must not be blank")
    @Size(min = 5, max = 2000, message = "Text must be between 5 and 2000 characters")
    private String text;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
