package com.neoprep.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoprep.domain.*;
import com.neoprep.dto.*;
import com.neoprep.exception.NotFoundException;
import com.neoprep.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
public class AdvancedFeatureService {

    private final UserProfileRepository userProfileRepository;
    private final DayPlanRepository dayPlanRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final ReadinessScoreRepository readinessScoreRepository;
    private final OutreachDraftRepository outreachDraftRepository;
    private final MockInterviewSessionRepository mockInterviewSessionRepository;
    private final SystemDesignCanvasSessionRepository systemDesignCanvasSessionRepository;
    private final WhiteboardAnalysisSessionRepository whiteboardAnalysisSessionRepository;
    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;

    public AdvancedFeatureService(UserProfileRepository userProfileRepository,
                                  DayPlanRepository dayPlanRepository,
                                  QuizAttemptRepository quizAttemptRepository,
                                  ReadinessScoreRepository readinessScoreRepository,
                                  OutreachDraftRepository outreachDraftRepository,
                                  MockInterviewSessionRepository mockInterviewSessionRepository,
                                  SystemDesignCanvasSessionRepository systemDesignCanvasSessionRepository,
                                  WhiteboardAnalysisSessionRepository whiteboardAnalysisSessionRepository,
                                  GeminiClient geminiClient,
                                  ObjectMapper objectMapper) {
        this.userProfileRepository = userProfileRepository;
        this.dayPlanRepository = dayPlanRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.readinessScoreRepository = readinessScoreRepository;
        this.outreachDraftRepository = outreachDraftRepository;
        this.mockInterviewSessionRepository = mockInterviewSessionRepository;
        this.systemDesignCanvasSessionRepository = systemDesignCanvasSessionRepository;
        this.whiteboardAnalysisSessionRepository = whiteboardAnalysisSessionRepository;
        this.geminiClient = geminiClient;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public WhiteboardAnalysisResponse analyzeWhiteboard(Long userId, byte[] imageBytes) {
        UserProfile user = getUser(userId);
        String imageBase64 = imageBytes == null ? null : Base64.getEncoder().encodeToString(imageBytes);

        String analysis;
        try {
            JsonNode root = objectMapper.readTree(geminiClient.analyzeWhiteboardJson(imageBase64));
            analysis = root.hasNonNull("analysis") ? root.get("analysis").asText() : null;
        } catch (Exception ex) {
            analysis = null;
        }

        if (analysis == null || analysis.isBlank()) {
            analysis = "Fallback analysis: check single points of failure, data consistency, and scaling bottlenecks.";
        }

        WhiteboardAnalysisSession session = new WhiteboardAnalysisSession();
        session.setUser(user);
        session.setImageBase64(imageBase64);
        session.setAnalysis(analysis);
        session = whiteboardAnalysisSessionRepository.save(session);

        return new WhiteboardAnalysisResponse(session.getId(), session.getAnalysis());
    }

    @Transactional
    public ReadinessResponseDto computeReadiness(Long userId, String company) {
        UserProfile user = getUser(userId);
        long completed = dayPlanRepository.countByRoadmapUserIdAndCompletedTrue(userId);
        double avgQuizRatio = quizAttemptRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId).stream()
                .filter(a -> a.getMaxScore() > 0)
                .mapToDouble(a -> (double) a.getScore() / a.getMaxScore())
                .average()
                .orElse(0.65);

        int score = (int) Math.max(10, Math.min(95, (completed * 2) + (avgQuizRatio * 40)));

        ReadinessScore readinessScore = new ReadinessScore();
        readinessScore.setUser(user);
        readinessScore.setCompany(company);
        readinessScore.setScorePercent(score);
        readinessScore.setTopGaps("System design articulation, Spring Boot security depth, and edge-case speed.");
        readinessScore.setNextActions("Run 2 timed mocks, solve 5 company-style problems, and review one architecture case.");
        readinessScore = readinessScoreRepository.save(readinessScore);

        return new ReadinessResponseDto(
                readinessScore.getId(),
                readinessScore.getCompany(),
                readinessScore.getScorePercent(),
                readinessScore.getTopGaps(),
                readinessScore.getNextActions()
        );
    }

    @Transactional
    public OutreachResponse generateOutreach(OutreachRequest request) {
        UserProfile user = getUser(request.userId());
        OutreachDraft draft = new OutreachDraft();
        draft.setUser(user);
        draft.setCompany(request.company());
        draft.setRole(request.role());
        draft.setChannel(request.channel());
        draft.setMessage("Hi Hiring Team, I built Java/Spring Boot + DSA projects aligned with " + request.company() +
                " stack and would love to contribute as " + request.role() + ".");
        draft = outreachDraftRepository.save(draft);
        return new OutreachResponse(draft.getId(), draft.getMessage());
    }

    @Transactional
    public MockInterviewResponse startMockInterview(MockInterviewRequest request) {
        UserProfile user = getUser(request.userId());
        MockInterviewSession session = new MockInterviewSession();
        session.setUser(user);
        session.setMode(request.mode());
        session.setStatus("STARTED");
        session.setPrompt("Explain your approach for " + request.topic() + " in under 2 minutes.");
        session.setFollowUpGuidance("If vague, expect interruptions on trade-offs and edge-case handling.");
        session = mockInterviewSessionRepository.save(session);
        return new MockInterviewResponse(session.getId(), session.getPrompt(), session.getFollowUpGuidance(), true);
    }

    @Transactional
    public SystemDesignCanvasResponse evaluateCanvas(SystemDesignCanvasRequest request) {
        UserProfile user = getUser(request.userId());
        SystemDesignCanvasSession session = new SystemDesignCanvasSession();
        session.setUser(user);
        session.setCanvasJson(request.canvasJson());
        session.setJarvisFeedback("Detected SPOF risk near DB node. Add replication and cache invalidation strategy.");
        session.setBeta(true);
        session = systemDesignCanvasSessionRepository.save(session);
        return new SystemDesignCanvasResponse(session.getId(), session.getJarvisFeedback(), true);
    }

    private UserProfile getUser(Long userId) {
        return userProfileRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
