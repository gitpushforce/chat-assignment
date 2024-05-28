package com.assignment.api.repository;

import com.assignment.api.entity.ChatParticipantsTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatParticipantsRepository extends JpaRepository<ChatParticipantsTbl, Long> {

    @Query("SELECT COUNT(cp) > 0 FROM ChatParticipantsTbl cp WHERE cp.chat.id = :chatId AND cp.user.id = :userId")
    Optional<Boolean> existsByChatIdAndUserId(@Param("chatId") Long chatId, @Param("userId") Long userId);
}
