package com.taipan.modchecker.repository;

import com.taipan.modchecker.model.MessageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageHistoryRepository extends JpaRepository<MessageHistory, Long> {
}