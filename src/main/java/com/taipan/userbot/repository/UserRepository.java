package com.taipan.modchecker.repository;

import com.taipan.modchecker.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByTelegramId(Long telegramId);
}