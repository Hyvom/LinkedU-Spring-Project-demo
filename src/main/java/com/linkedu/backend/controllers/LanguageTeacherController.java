package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.Question;
import com.linkedu.backend.dto.QuestionFormDTO;
import com.linkedu.backend.repositories.QuestionRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class LanguageTeacherController {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Question> createQuestion(
            @RequestParam Long teacherId,
            @RequestBody QuestionFormDTO dto) {

        Question question = new Question();
        question.setQuestionText(dto.getQuestionText());
        question.setOptionA(dto.getOptionA());
        question.setOptionB(dto.getOptionB());
        question.setOptionC(dto.getOptionC());
        question.setOptionD(dto.getOptionD());
        question.setCorrectOption(dto.getCorrectOption());
        question.setCreatedBy(userRepository.findById(teacherId).orElseThrow());

        return ResponseEntity.ok(questionRepository.save(question));
    }
}
