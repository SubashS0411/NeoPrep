package com.neoprep.service;

import com.neoprep.domain.*;
import com.neoprep.dto.CodeEvaluationNormalized;
import com.neoprep.dto.CodeEvaluationResponseDto;
import com.neoprep.dto.CodeOptimizationNormalized;
import com.neoprep.dto.CodeOptimizeResponseDto;
import com.neoprep.exception.BadRequestException;
import com.neoprep.exception.NotFoundException;
import com.neoprep.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;

@Service
public class CodeEvaluationService {

    private final UserProfileRepository userProfileRepository;
    private final DayPlanRepository dayPlanRepository;
    private final SubmissionRepository submissionRepository;
    private final EvaluationRepository evaluationRepository;
    private final GeminiClient geminiClient;
    private final CodeEvaluationJsonParser codeEvaluationJsonParser;
    private final CodeOptimizationJsonParser codeOptimizationJsonParser;
    private final CodeOptimizationAttemptRepository codeOptimizationAttemptRepository;

    public CodeEvaluationService(UserProfileRepository userProfileRepository,
                                 DayPlanRepository dayPlanRepository,
                                 SubmissionRepository submissionRepository,
                                 EvaluationRepository evaluationRepository,
                                 GeminiClient geminiClient,
                                 CodeEvaluationJsonParser codeEvaluationJsonParser,
                                 CodeOptimizationJsonParser codeOptimizationJsonParser,
                                 CodeOptimizationAttemptRepository codeOptimizationAttemptRepository) {
        this.userProfileRepository = userProfileRepository;
        this.dayPlanRepository = dayPlanRepository;
        this.submissionRepository = submissionRepository;
        this.evaluationRepository = evaluationRepository;
        this.geminiClient = geminiClient;
        this.codeEvaluationJsonParser = codeEvaluationJsonParser;
        this.codeOptimizationJsonParser = codeOptimizationJsonParser;
        this.codeOptimizationAttemptRepository = codeOptimizationAttemptRepository;
    }

    @Transactional
    public CodeEvaluationResponseDto evaluate(Long userId, Long dayPlanId, String problemName, String codeText, byte[] imageBytes) {
        if ((codeText == null || codeText.isBlank()) && (imageBytes == null || imageBytes.length == 0)) {
            throw new BadRequestException("Provide either codeText or image file.");
        }

        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        DayPlan dayPlan = null;
        if (dayPlanId != null) {
            dayPlan = dayPlanRepository.findById(dayPlanId)
                    .orElseThrow(() -> new NotFoundException("Day plan not found"));
        }

        Submission submission = new Submission();
        submission.setUser(user);
        submission.setDayPlan(dayPlan);
        submission.setProblemName(problemName);

        String imageBase64 = null;
        if (imageBytes != null && imageBytes.length > 0) {
            imageBase64 = Base64.getEncoder().encodeToString(imageBytes);
            submission.setSubmissionType(SubmissionType.IMAGE);
            submission.setImageBase64(imageBase64);
        } else {
            submission.setSubmissionType(SubmissionType.TEXT);
        }
        submission.setCodeText(codeText);
        submission = submissionRepository.save(submission);

        CodeEvaluationNormalized normalized;
        try {
            String raw = geminiClient.evaluateCodeJson(problemName, codeText, imageBase64);
            normalized = codeEvaluationJsonParser.parseAndValidate(raw);
        } catch (Exception ex) {
            normalized = new CodeEvaluationNormalized(
                    "O(n)",
                    "O(n)",
                    "Evaluator fallback used due to transient model error.",
                    "Refactor with clear loop invariants and edge-case handling.",
                    "Two Pointers"
            );
        }

        Evaluation evaluation = new Evaluation();
        evaluation.setSubmission(submission);
        evaluation.setTimeComplexity(normalized.timeComplexity());
        evaluation.setSpaceComplexity(normalized.spaceComplexity());
        evaluation.setBugs(normalized.bugs());
        evaluation.setOptimalSolution(normalized.optimalSolution());
        evaluation.setAlgorithmPattern(normalized.algorithmPattern());
        evaluation = evaluationRepository.save(evaluation);

        return new CodeEvaluationResponseDto(
                submission.getId(),
                evaluation.getId(),
                evaluation.getTimeComplexity(),
                evaluation.getSpaceComplexity(),
                evaluation.getBugs(),
                evaluation.getOptimalSolution(),
                evaluation.getAlgorithmPattern()
        );
    }

    @Transactional
    public CodeOptimizeResponseDto optimize(Long userId, String problemName, String codeText, byte[] imageBytes) {
        if ((codeText == null || codeText.isBlank()) && (imageBytes == null || imageBytes.length == 0)) {
            throw new BadRequestException("Provide either codeText or image file.");
        }

        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String imageBase64 = imageBytes != null && imageBytes.length > 0
                ? Base64.getEncoder().encodeToString(imageBytes)
                : null;

        CodeOptimizationNormalized normalized;
        try {
            normalized = codeOptimizationJsonParser.parseAndValidate(
                    geminiClient.optimizeCodeJson(problemName, codeText, imageBase64)
            );
        } catch (Exception ex) {
            normalized = new CodeOptimizationNormalized(
                    "Fallback: reduce temporary object allocations.",
                    "Fallback: remove nested loops where possible.",
                    "Fallback: add stronger input validation.",
                    "Fallback: break function into smaller methods.",
                    "Fallback: line-level optimization unavailable due to transient model issue."
            );
        }

        String previousComplexity = codeOptimizationAttemptRepository
                .findTopByUserIdAndProblemNameOrderByCreatedAtDesc(userId, problemName)
                .map(CodeOptimizationAttempt::getComplexityFeedback)
                .orElse("No previous attempt.");

        CodeOptimizationAttempt attempt = new CodeOptimizationAttempt();
        attempt.setUser(user);
        attempt.setProblemName(problemName);
        attempt.setCodeText(codeText);
        attempt.setImageBase64(imageBase64);
        attempt.setMemoryFeedback(normalized.memoryFeedback());
        attempt.setComplexityFeedback(normalized.complexityFeedback());
        attempt.setCorrectnessFeedback(normalized.correctnessFeedback());
        attempt.setReadabilityFeedback(normalized.readabilityFeedback());
        attempt.setLineByLineFeedback(normalized.lineByLineFeedback());
        attempt.setImprovementDelta("Previous complexity hint: " + previousComplexity + " -> Current: " + normalized.complexityFeedback());
        attempt = codeOptimizationAttemptRepository.save(attempt);

        return new CodeOptimizeResponseDto(
                attempt.getId(),
                attempt.getMemoryFeedback(),
                attempt.getComplexityFeedback(),
                attempt.getCorrectnessFeedback(),
                attempt.getReadabilityFeedback(),
                attempt.getLineByLineFeedback(),
                attempt.getImprovementDelta()
        );
    }
}
