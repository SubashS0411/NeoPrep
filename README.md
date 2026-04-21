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
- `POST /api/roadmaps/{roadmapId}/recalculate` - adaptive roadmap recalculation from missed days + weak topics
- `GET /api/roadmaps/{roadmapId}` - full roadmap
- `GET /api/roadmaps/{roadmapId}/days/{dayNumber}` - daily task
- `POST /api/roadmaps/{roadmapId}/days/{dayNumber}/complete` - mark completed
- `POST /api/submissions/evaluate` - multimodal Java code evaluation (text/image)
- `POST /api/submissions/optimize` - line-level optimize feedback (memory/complexity/correctness/readability)
- `GET /api/progress/{userId}` - progress and streak
- `POST /api/achievements/{userId}/streak7` - unlock 7-day streak achievement
- `GET /api/achievements/{userId}` - list achievements
- `GET /api/pattern-vault` / `POST /api/pattern-vault` - pattern vault CRUD
- `POST /api/pattern-vault/ingest` - batch ingest company patterns
- `POST /api/pattern-vault/generate` - generate company-style practice
- `POST /api/standups/{userId}/generate` / `GET /api/standups/{userId}` - daily stand-up generation/feed
- `POST /api/nexus/sync` - orchestration workflow for adaptive recalc + stand-up + streak trigger signal
- `POST /api/advanced/whiteboard/analyze` - multimodal whiteboard logic analysis
- `GET /api/advanced/readiness/{userId}?company=...` - predictive placement odds
- `POST /api/advanced/outreach/draft` - recruiter outreach draft generation (scoped role required)
- `POST /api/advanced/interviews/mock/start` - voice-mock session bootstrap (scoped role required)
- `POST /api/advanced/canvas/evaluate` - shadow system design canvas beta evaluation

All API routes require `X-API-KEY` header (default `local-dev-key`).
Sensitive advanced endpoints also require `X-ROLE` with value `advanced-agent` (configurable).
