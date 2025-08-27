package com.taipan.userbot.repository;

import com.taipan.userbot.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByTelegramId(Long telegramId);

    UserEntity findByUsername(String cleanUsername);
}