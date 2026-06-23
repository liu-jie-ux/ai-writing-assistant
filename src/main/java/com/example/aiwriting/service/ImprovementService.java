package com.example.aiwriting.service;

import com.example.aiwriting.dto.ImproveRequest;
import com.example.aiwriting.dto.ImproveResponse;
import com.example.aiwriting.model.ImprovementRecord;
import com.example.aiwriting.repository.ImprovementRecordRepository;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImprovementService {

    private final DeepSeekService deepSeekService;
    private final ImprovementRecordRepository repository;

    public ImprovementService(DeepSeekService deepSeekService, ImprovementRecordRepository repository) {
        this.deepSeekService = deepSeekService;
        this.repository = repository;
    }

    public ImproveResponse improve(ImproveRequest request) throws Exception {
        if (request.getText() == null || request.getText().isBlank()) {
            throw new IllegalArgumentException("text must not be empty");
        }

        JsonNode result = deepSeekService.improveText(request.getText());

        ImprovementRecord record = new ImprovementRecord();
        record.setOriginalText(request.getText());
        record.setFormalText(result.get("formal").asText());
        record.setNaturalText(result.get("natural").asText());
        record.setBusinessText(result.get("business").asText());
        ImprovementRecord saved = repository.save(record);

        return new ImproveResponse(
                saved.getId(),
                saved.getOriginalText(),
                saved.getFormalText(),
                saved.getNaturalText(),
                saved.getBusinessText(),
                saved.getCreatedAt()
        );
    }

    public List<ImproveResponse> getHistory() {
        return repository.findAll().stream()
                .map(r -> new ImproveResponse(
                        r.getId(),
                        r.getOriginalText(),
                        r.getFormalText(),
                        r.getNaturalText(),
                        r.getBusinessText(),
                        r.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
}
