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

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found: " + senderId));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found: " + receiverId));

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
            @RequestParam Long user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User not found: " + user1Id));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User not found: " + user2Id));
        List<ChatMessage> messages = chatMessageRepository.findConversation(user1, user2);
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
        return userRepository.findById(userId)
                .map(user -> ResponseEntity.ok(chatMessageRepository.findByReceiverAndSeenFalse(user)))
                .orElse(ResponseEntity.ok(List.of()));
    }
}