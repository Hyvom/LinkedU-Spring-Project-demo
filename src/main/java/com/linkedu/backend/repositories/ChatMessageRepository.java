package com.linkedu.backend.repositories;

import com.linkedu.backend.entities.ChatMessage;
import com.linkedu.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySenderAndReceiverOrderByTimestampAsc(User sender, User receiver);
    List<ChatMessage> findByReceiverAndSenderOrderByTimestampAsc(User receiver, User sender);
    List<ChatMessage> findByReceiverAndSeenFalse(User receiver);

    @Query(
            "SELECT m FROM ChatMessage m WHERE " +
                    "(m.sender = :user1 AND m.receiver = :user2) OR " +
                    "(m.sender = :user2 AND m.receiver = :user1) " +
                    "ORDER BY m.timestamp ASC"
    )
    List<ChatMessage> findConversation(
            @Param("user1") User user1,
            @Param("user2") User user2
    );
}
