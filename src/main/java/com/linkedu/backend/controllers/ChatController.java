package com.linkedu.backend.controllers;

import com.linkedu.backend.entities.ChatMessage;
import com.linkedu.backend.entities.User;
import com.linkedu.backend.repositories.ChatMessageRepository;
import com.linkedu.backend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    // CREATE - Send message
    @PostMapping
    public ResponseEntity<ChatMessage> sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String message) {

        User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();

        ChatMessage chat = new ChatMessage();
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setMessage(message);

        return ResponseEntity.ok(chatMessageRepository.save(chat));
    }

    // READ - Get conversation between 2 users
    @GetMapping("/conversation")
    public ResponseEntity<List<ChatMessage>> getConversation(
            @RequestParam Long user1Id,
            @RequestParam Long user2Id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate) {
            //@Optional @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate) {

        List<ChatMessage> messages = chatMessageRepository.findBySenderAndReceiverOrderByTimestampAsc(
                userRepository.findById(user1Id).orElseThrow(),
                userRepository.findById(user2Id).orElseThrow()
        );

        if (fromDate != null) {
            messages = messages.stream()
                    .filter(m -> m.getTimestamp().isAfter(fromDate))
                    .toList();
        }

        return ResponseEntity.ok(messages);
    }

    // READ - Mark messages as seen
    @PutMapping("/{id}/seen")
    public ResponseEntity<ChatMessage> markAsSeen(@PathVariable Long id) {
        ChatMessage message = chatMessageRepository.findById(id).orElseThrow();
        message.setSeen(true);
        return ResponseEntity.ok(chatMessageRepository.save(message));
    }

    // READ - Get unread messages for user
    @GetMapping("/unread/{userId}")
    public ResponseEntity<List<ChatMessage>> getUnreadMessages(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return ResponseEntity.ok(chatMessageRepository.findByReceiverAndSeenFalse(user));
    }
}