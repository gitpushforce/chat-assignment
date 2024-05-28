package com.assignment.api.repository;

import com.assignment.api.entity.ChatTbl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<ChatTbl, Long> {
}
