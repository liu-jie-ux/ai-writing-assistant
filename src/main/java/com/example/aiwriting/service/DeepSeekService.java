package com.example.aiwriting.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class DeepSeekService {

    @Value("${claude.api.key}")
    private String apiKey;

    @Value("${claude.api.url}")
    private String apiUrl;

    @Value("${claude.model}")
    private String model;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode improveText(String inputText) throws IOException {
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("model", model);
        requestBody.put("max_tokens", 1024);

        // JSON形式での返答を強制するオプション
        ObjectNode responseFormat = objectMapper.createObjectNode();
        responseFormat.put("type", "json_object");
        requestBody.set("response_format", responseFormat);

        ArrayNode messages = objectMapper.createArrayNode();

        ObjectNode systemMessage = objectMapper.createObjectNode();
        systemMessage.put("role", "system");
        systemMessage.put("content",
            "You are a professional writing assistant. " +
            "You must always respond with valid JSON only. No explanation, no markdown, no extra text."
        );
        messages.add(systemMessage);

        ObjectNode userMessage = objectMapper.createObjectNode();
        userMessage.put("role", "user");
        userMessage.put("content",
            "Rewrite the following text in three styles. " +
            "Return ONLY a JSON object with exactly these four keys: original, formal, natural, business.\n\n" +
            "- formal: polished and professional tone\n" +
            "- natural: friendly and conversational tone\n" +
            "- business: concise corporate tone\n\n" +
            "Input text: " + inputText + "\n\n" +
            "Example output format:\n" +
            "{\"original\": \"<input>\", \"formal\": \"<formal version>\", \"natural\": \"<natural version>\", \"business\": \"<business version>\"}"
        );
        messages.add(userMessage);
        requestBody.set("messages", messages);

        Request request = new Request.Builder()
                .url(apiUrl)
                .post(RequestBody.create(
                        objectMapper.writeValueAsString(requestBody),
                        MediaType.parse("application/json")
                ))
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("DeepSeek API error: " + response.code() + " " + response.body().string());
            }
            JsonNode responseJson = objectMapper.readTree(response.body().string());
            String content = responseJson.get("choices").get(0).get("message").get("content").asText();

            // AIが返したJSON文字列をパースして返す
            JsonNode result = objectMapper.readTree(content);

            // 必須キーの存在チェック
            if (!result.has("formal") || !result.has("natural") || !result.has("business")) {
                throw new IOException("AI response missing required keys. Got: " + content);
            }

            return result;
        }
    }
}
