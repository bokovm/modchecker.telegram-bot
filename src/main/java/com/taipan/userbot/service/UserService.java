package com.taipan.userbot.service;

import com.taipan.userbot.model.UserEntity;
import com.taipan.userbot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UserEntity getOrCreateUser(User tgUser) {
        try {
            // Поиск пользователя по telegramId
            UserEntity user = userRepository.findByTelegramId(tgUser.getId());

            if (user == null) {
                // Создание нового пользователя
                user = new UserEntity();
                user.setTelegramId(tgUser.getId());
                user.setUsername(tgUser.getUserName());
                user.setFirstName(tgUser.getFirstName());
                user.setLastName(tgUser.getLastName());

                // Сохранение в БД
                user = userRepository.save(user);
                logger.info("✅ Created new user: ID={}, Username={}", user.getId(), user.getUsername());
            } else {
                // Проверка и обновление данных, если они изменились
                boolean updated = false;

                if (!isEqual(user.getUsername(), tgUser.getUserName())) {
                    user.setUsername(tgUser.getUserName());
                    updated = true;
                }
                if (!isEqual(user.getFirstName(), tgUser.getFirstName())) {
                    user.setFirstName(tgUser.getFirstName());
                    updated = true;
                }
                if (!isEqual(user.getLastName(), tgUser.getLastName())) {
                    user.setLastName(tgUser.getLastName());
                    updated = true;
                }

                if (updated) {
                    user = userRepository.save(user);
                    logger.info("🔄 Updated user: ID={}, Username={}", user.getId(), user.getUsername());
                }
            }
            return user;
        } catch (Exception e) {
            logger.error("❌ Error saving user: {}", tgUser.getId(), e);
            throw new RuntimeException("Failed to save user", e);
        }
    }

    private boolean isEqual(String str1, String str2) {
        if (str1 == null) return str2 == null;
        return str1.equals(str2);
    }

    // Дополнительные методы при необходимости
    public long countUsers() {
        return userRepository.count();
    }
}