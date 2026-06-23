package com.example.aiwriting.controller;

import com.example.aiwriting.dto.ImproveRequest;
import com.example.aiwriting.dto.ImproveResponse;
import com.example.aiwriting.service.ImprovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImprovementController {

    private final ImprovementService improvementService;

    public ImprovementController(ImprovementService improvementService) {
        this.improvementService = improvementService;
    }

    @PostMapping("/improve")
    public ResponseEntity<?> improve(@RequestBody ImproveRequest request) {
        try {
            ImproveResponse response = improvementService.improve(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("error", "Internal error: " + e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<ImproveResponse>> history() {
        return ResponseEntity.ok(improvementService.getHistory());
    }
}
