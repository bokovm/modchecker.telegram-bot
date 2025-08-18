package com.taipan.modchecker.service;

import com.taipan.modchecker.model.MessageHistory;
import com.taipan.modchecker.repository.MessageHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageHistoryService {
    private final MessageHistoryRepository messageHistoryRepository;

    public MessageHistoryService(MessageHistoryRepository messageHistoryRepository) {
        this.messageHistoryRepository = messageHistoryRepository;
    }

    @Transactional
    public void saveMessage(MessageHistory message) {
        messageHistoryRepository.save(message);
    }
}