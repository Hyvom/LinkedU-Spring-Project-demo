package com.linkedu.backend.Repositories;

import com.linkedu.backend.Entities.ChatMessage;
import com.linkedu.backend.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiverOrderByTimestampAsc(User sender, User receiver);
    List<ChatMessage> findByReceiverAndSenderOrderByTimestampAsc(User receiver, User sender);
}
