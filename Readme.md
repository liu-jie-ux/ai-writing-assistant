# AI Writing Assistant

A Spring Boot web app that uses the **DeepSeek API** to rewrite English text in three different styles: **Formal**, **Natural**, and **Business**.

## What It Does

1. You type or paste English text into the browser
2. DeepSeek AI rewrites it in three styles at once
3. Results appear with a typewriter animation
4. You can copy any version with one click
5. All history is saved and searchable

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.5.0 |
| Database | H2 (in-memory) |
| HTTP Client | OkHttp 4.12.0 |
| Validation | Hibernate Validator |
| Logging | SLF4J |
| AI | DeepSeek Chat API |

## Project Structure

```
ai-writing-assistant/
├── pom.xml
└── src/main/
    ├── java/com/example/aiwriting/
    │   ├── AiWritingAssistantApplication.java
    │   ├── model/
    │   │   └── ImprovementRecord.java          # JPA entity (4 text fields)
    │   ├── repository/
    │   │   └── ImprovementRecordRepository.java # Spring Data JPA
    │   ├── dto/
    │   │   ├── ImproveRequest.java              # Request DTO (@Valid)
    │   │   └── ImproveResponse.java             # Response DTO (4 keys)
    │   ├── service/
    │   │   ├── DeepSeekService.java             # DeepSeek API calls
    │   │   └── ImprovementService.java          # Business logic
    │   └── controller/
    │       ├── ImprovementController.java       # REST endpoints + logging
    │       └── GlobalExceptionHandler.java      # Unified error handling
    └── resources/
        ├── application.properties
        └── static/
            └── index.html                       # Frontend UI
```

## API Endpoints

### `POST /api/improve` — Improve Text

```bash
curl -X POST http://localhost:8080/api/improve \
  -H "Content-Type: application/json" \
  -d '{"text": "I think this product is kind of good but maybe could be better."}'
```

**Success (200):**
```json
{
  "id": 1,
  "original": "I think this product is kind of good but maybe could be better.",
  "formal": "I believe this product is acceptable, though there may be room for improvement.",
  "natural": "I think this product is pretty good, but it could be a bit better.",
  "business": "This product is satisfactory, but there is room for enhancement.",
  "createdAt": "2026-06-23T10:47:13"
}
```

**Validation error — too short (400):**
```bash
curl -X POST http://localhost:8080/api/improve \
  -H "Content-Type: application/json" \
  -d '{"text": "hi"}'
# => {"error": "text: Text must be between 5 and 2000 characters"}
```

**Validation error — empty (400):**
```bash
curl -X POST http://localhost:8080/api/improve \
  -H "Content-Type: application/json" \
  -d '{"text": ""}'
# => {"error": "text: Text must not be blank; text: Text must be between 5 and 2000 characters"}
```

**Validation rules:** Text is required, cannot be blank, must be 5–2000 characters.

### `GET /api/history` — View History

```bash
curl http://localhost:8080/api/history
# => [{ "id": 1, "original": "...", "formal": "...", "natural": "...", "business": "...", "createdAt": "..." }, ...]
```

## Browser UI

Open **http://localhost:8080** to see:

- **Input card** — white panel with textarea and live character counter
- **Three robot cards** — Formal 🎩 (blue), Natural 💁‍♂️ (amber), Business 💼 (green)
- **Color accent bars** at the top of each card
- **Typewriter animation** — results appear character by character
- **Bouncing dot animation** while waiting for the AI response
- **Copy button** on each card — click to copy, shows "✓ Copied!" for 2 seconds
- **History section** at the bottom — search across all fields, matching words highlighted

## Quick Start

### Prerequisites

1. **Install JDK 17+**
   ```bash
   winget install EclipseAdoptium.Temurin.17.JDK
   ```

2. **Get a DeepSeek API key** from https://platform.deepseek.com/

### Run the App

```bash
git clone https://github.com/liu-jie-ux/ai-writing-assistant.git
cd ai-writing-assistant
set DEEPSEEK_API_KEY=sk-your-key-here
mvnw.cmd spring-boot:run
```

Then open **http://localhost:8080** in your browser.

### Build (optional)

```bash
mvnw.cmd clean package -DskipTests
```

## Version History

| Version | What Changed |
|---|---|
| V1 | Single style improvement (`improvedText`) |
| V2 | Multi-style: `formal`, `natural`, `business` — structured JSON response |
| V3 | Dark theme frontend with robot cards |
| V4 | Light theme redesign, character counter, SVG avatar |
| **V5 (current)** | Validation, global error handler, structured logging, Copy buttons, History search |

## License

MIT
