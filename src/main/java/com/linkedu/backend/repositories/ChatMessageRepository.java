package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.ChatMessage;
import com.linkedu.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiverOrderByTimestampAsc(User sender, User receiver);
    List<ChatMessage> findByReceiverAndSenderOrderByTimestampAsc(User receiver, User sender);
    List<ChatMessage> findByReceiverAndSeenFalse(User receiver);
}
