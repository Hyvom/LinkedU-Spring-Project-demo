package com.linkedu.backend.controllers;

import com.linkedu.backend.dto.ChatbotAnswerResponse;
import com.linkedu.backend.dto.ChatbotAskRequest;
import com.linkedu.backend.services.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/ask")
    public ResponseEntity<ChatbotAnswerResponse> ask(@RequestBody ChatbotAskRequest request) {
        String answer = chatbotService.ask(request != null ? request.question() : null);
        return ResponseEntity.ok(new ChatbotAnswerResponse(answer));
    }
}