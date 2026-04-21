package com.neoprep.service;

import com.neoprep.domain.*;
import com.neoprep.dto.CodeEvaluationNormalized;
import com.neoprep.dto.CodeEvaluationResponseDto;
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

    public CodeEvaluationService(UserProfileRepository userProfileRepository,
                                 DayPlanRepository dayPlanRepository,
                                 SubmissionRepository submissionRepository,
                                 EvaluationRepository evaluationRepository,
                                 GeminiClient geminiClient,
                                 CodeEvaluationJsonParser codeEvaluationJsonParser) {
        this.userProfileRepository = userProfileRepository;
        this.dayPlanRepository = dayPlanRepository;
        this.submissionRepository = submissionRepository;
        this.evaluationRepository = evaluationRepository;
        this.geminiClient = geminiClient;
        this.codeEvaluationJsonParser = codeEvaluationJsonParser;
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

        String raw = geminiClient.evaluateCodeJson(problemName, codeText, imageBase64);
        CodeEvaluationNormalized normalized = codeEvaluationJsonParser.parseAndValidate(raw);

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
}
