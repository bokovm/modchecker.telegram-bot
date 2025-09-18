package com.taipan.adminbot.service;

import com.taipan.adminbot.repository.AdminRepository;
import com.taipan.shared.service.BotManagerService;
import com.taipan.userbot.model.UserEntity;
import com.taipan.userbot.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminSendService {

    private final BotManagerService botManager;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AdminSendService(BotManagerService botManager,
                            UserRepository userRepository,
                            AdminRepository adminRepository) {
        this.botManager = botManager;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    public boolean isAdmin(Long telegramId) {
        return adminRepository.existsByTelegramId(telegramId);
    }

    public String sendToUser(String username, String botName, String text) {
        String cleanUsername = username.startsWith("@") ? username.substring(1) : username;
        UserEntity user = userRepository.findByUsername(cleanUsername);

        if (user == null) {
            return "❌ Пользователь @" + cleanUsername + " не найден";
        }

        boolean success = botManager.sendAsBot(botName, user.getTelegramId(), text);
        return success ?
                "✅ Сообщение отправлено @" + cleanUsername :
                "❌ Ошибка отправки через бота " + botName;
    }

    public String broadcast(String botName, String text) {
        Iterable<UserEntity> users = userRepository.findAll();
        int successCount = 0;
        int totalCount = 0;

        for (UserEntity user : users) {
            totalCount++;
            if (botManager.sendAsBot(botName, user.getTelegramId(), text)) {
                successCount++;
            }
            try {
                Thread.sleep(50); // Защита от лимитов Telegram
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return String.format("✅ Рассылка завершена!\nОтправлено: %d/%d пользователей", successCount, totalCount);
    }
}
