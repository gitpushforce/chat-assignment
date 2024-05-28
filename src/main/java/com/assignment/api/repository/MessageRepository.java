package com.assignment.api.repository;

import com.assignment.api.entity.MessageTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<MessageTbl, Long> {

    Optional<List<MessageTbl>> findAllByChatId(Long chatId);
}
