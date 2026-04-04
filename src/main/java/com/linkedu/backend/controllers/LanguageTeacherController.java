package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.Question;
import com.linkedu.backend.entities.Quiz;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.dto.QuestionResponseDTO;
import com.linkedu.backend.dto.QuestionFormDTO;
import com.linkedu.backend.repositories.QuestionRepository;
import com.linkedu.backend.repositories.QuizRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class LanguageTeacherController {

    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<QuestionResponseDTO> createQuestion(
            @RequestParam Long teacherId,
            @RequestBody QuestionFormDTO dto) {
        User teacher = userRepository.findById(teacherId).orElseThrow();
        Quiz quiz = quizRepository.findById(dto.getQuizId()).orElseThrow();
        if (!quiz.getCreatedBy().getId().equals(teacher.getId())) {
            throw new IllegalArgumentException("Teacher can only add questions to owned quizzes");
        }

        Question question = new Question();
        question.setQuiz(quiz);
        question.setQuestionText(dto.getQuestionText());
        question.setOptionA(dto.getOptionA());
        question.setOptionB(dto.getOptionB());
        question.setOptionC(dto.getOptionC());
        question.setOptionD(dto.getOptionD());
        question.setCorrectOption(dto.getCorrectOption());
        question.setCreatedBy(teacher);

        Question savedQuestion = questionRepository.save(question);
        return ResponseEntity.ok(toResponseDto(savedQuestion));
    }

    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuizQuestions(
            @PathVariable Long quizId,
            @RequestParam Long teacherId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow();
        if (!quiz.getCreatedBy().getId().equals(teacherId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<QuestionResponseDTO> questions = questionRepository.findByQuizId(quizId)
                .stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(questions);
    }

    @DeleteMapping("/{questionId}")
    public ResponseEntity<?> deleteQuestion(
            @PathVariable Long questionId,
            @RequestParam Long teacherId) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        if (question.getCreatedBy() == null || !question.getCreatedBy().getId().equals(teacherId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You can delete only your own questions"));
        }
        questionRepository.delete(question);
        return ResponseEntity.ok(Map.of("message", "Question deleted"));
    }

    private QuestionResponseDTO toResponseDto(Question question) {
        return new QuestionResponseDTO(
                question.getId(),
                question.getQuiz() != null ? question.getQuiz().getId() : null,
                question.getQuestionText(),
                question.getOptionA(),
                question.getOptionB(),
                question.getOptionC(),
                question.getOptionD(),
                question.getCorrectOption()
        );
    }
}
