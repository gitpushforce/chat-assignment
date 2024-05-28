package com.assignment.api.repository;

import com.assignment.api.entity.ReadMessageTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadMessageRepository extends JpaRepository<ReadMessageTbl, Long> {

    @Query("SELECT COUNT(m) FROM ReadMessageTbl m WHERE m.message.id = :messageId")
    Integer countByMessageId(@Param("messageId") Long MessageId);

    @Query("SELECT COUNT(cp) FROM ChatParticipantsTbl cp WHERE cp.chat.id = :chatId")
    Integer countParticipantsByChatId(@Param("chatId") Long chatId);

    @Query("SELECT COUNT(m) > 0 FROM ReadMessageTbl m WHERE m.message.id = :messageId AND m.reader.id = :requesterId")
    boolean existsByMessageIdAndUserId(@Param("messageId") Long messageId, @Param("requesterId") Long requesterId);
}
