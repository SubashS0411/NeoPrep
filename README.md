# NeoPrep

Agentic placement-copilot MVP for 35-day Java interview preparation.

## Stack
- Backend: Spring Boot 3 (Java 17), PostgreSQL
- AI integration: Gemini-compatible client interface with strict JSON parsing/validation
- Frontend shell: HUD-style static web shell (`/frontend`) consuming backend APIs

## Run backend
```bash
mvn spring-boot:run
```

## Test
```bash
mvn test
```

## Core APIs
- `POST /api/onboarding/roadmap` - generate 35-day roadmap from JD
- `GET /api/roadmaps/{roadmapId}` - full roadmap
- `GET /api/roadmaps/{roadmapId}/days/{dayNumber}` - daily task
- `POST /api/roadmaps/{roadmapId}/days/{dayNumber}/complete` - mark completed
- `POST /api/submissions/evaluate` - multimodal Java code evaluation (text/image)
- `GET /api/progress/{userId}` - progress and streak
- `POST /api/achievements/{userId}/streak7` - unlock 7-day streak achievement
- `GET /api/achievements/{userId}` - list achievements

All API routes require `X-API-KEY` header (default `local-dev-key`).
