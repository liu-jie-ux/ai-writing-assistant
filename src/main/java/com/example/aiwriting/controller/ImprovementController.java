package com.example.aiwriting.controller;

import com.example.aiwriting.dto.ImproveRequest;
import com.example.aiwriting.dto.ImproveResponse;
import com.example.aiwriting.service.ImprovementService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ImprovementController {

    private static final Logger log = LoggerFactory.getLogger(ImprovementController.class);

    private final ImprovementService improvementService;

    public ImprovementController(ImprovementService improvementService) {
        this.improvementService = improvementService;
    }

    @PostMapping("/improve")
    public ResponseEntity<?> improve(@Valid @RequestBody ImproveRequest request) {
        log.info("POST /api/improve — input length: {} chars", request.getText().length());
        long start = System.currentTimeMillis();

        try {
            ImproveResponse response = improvementService.improve(request);
            log.info("POST /api/improve — completed in {}ms, saved id: {}", System.currentTimeMillis() - start, response.getId());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("POST /api/improve — unexpected error: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Internal error: " + e.getMessage()));
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<ImproveResponse>> history() {
        log.info("GET /api/history");
        return ResponseEntity.ok(improvementService.getHistory());
    }
}
