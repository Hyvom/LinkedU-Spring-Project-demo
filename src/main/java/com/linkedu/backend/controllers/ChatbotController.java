package com.linkedu.backend.controllers;

import com.linkedu.backend.dto.ChatbotRequest;
import com.linkedu.backend.dto.ChatbotResponse;
import com.linkedu.backend.services.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

    private final ChatbotService chatbotService;

    @PostMapping("/chat")
    public ResponseEntity<ChatbotResponse> chat(@RequestBody ChatbotRequest request) {
        String reply = chatbotService.chat(request.getMessage());
        return ResponseEntity.ok(new ChatbotResponse(reply));
    }
}