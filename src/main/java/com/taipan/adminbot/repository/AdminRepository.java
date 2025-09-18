package com.taipan.adminbot.repository;

import com.taipan.adminbot.model.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<AdminEntity, Long> {
    AdminEntity findByTelegramId(Long telegramId);
    boolean existsByTelegramId(Long telegramId);
}