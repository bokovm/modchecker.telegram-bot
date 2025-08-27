package com.taipan.adminbot.command;

import com.taipan.adminbot.AdminBotApplication;
import com.taipan.adminbot.repository.AdminRepository;
import com.taipan.shared.service.BotManagerService;
import com.taipan.userbot.model.UserEntity;
import com.taipan.userbot.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class AdminSendCommand {
    private final BotManagerService botManager;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;

    public AdminSendCommand(BotManagerService botManager,
                            UserRepository userRepository,
                            AdminRepository adminRepository) {
        this.botManager = botManager;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
    }

    public void handle(Message message, AdminBotApplication adminBot) {
        // Проверка прав администратора
        Long senderId = message.getFrom().getId();
        if (!isAdmin(senderId)) {
            sendResponse(adminBot, message.getChatId(), "❌ Недостаточно прав. Только администраторы могут использовать эту команду.");
            return;
        }

        String[] args = message.getText().split(" ", 4);

        if (args.length < 4) {
            sendResponse(adminBot, message.getChatId(), "❌ Формат: /sendmessage [@username|all] [bot-name] text");
            return;
        }

        String target = args[1];
        String botName = args[2];
        String text = args[3];

        if ("all".equalsIgnoreCase(target)) {
            broadcast(botName, text, message.getChatId(), adminBot);
        } else {
            sendToUser(target, botName, text, message.getChatId(), adminBot);
        }
    }

    private boolean isAdmin(Long telegramId) {
        return adminRepository.existsByTelegramId(telegramId);
    }

    // Остальные методы без изменений...
    private void sendToUser(String username, String botName, String text, Long adminChatId, AdminBotApplication adminBot) {
        String cleanUsername = username.startsWith("@") ? username.substring(1) : username;
        UserEntity user = userRepository.findByUsername(cleanUsername);

        if (user == null) {
            sendResponse(adminBot, adminChatId, "❌ Пользователь @" + cleanUsername + " не найден");
            return;
        }

        boolean success = botManager.sendAsBot(botName, user.getTelegramId(), text);
        String response = success ?
                "✅ Сообщение отправлено @" + cleanUsername :
                "❌ Ошибка отправки через бота " + botName;

        sendResponse(adminBot, adminChatId, response);
    }

    private void broadcast(String botName, String text, Long adminChatId, AdminBotApplication adminBot) {
        Iterable<UserEntity> users = userRepository.findAll();
        int successCount = 0;
        int totalCount = 0;

        for (UserEntity user : users) {
            totalCount++;
            if (botManager.sendAsBot(botName, user.getTelegramId(), text)) {
                successCount++;
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        String response = String.format("✅ Рассылка завершена!\nОтправлено: %d/%d пользователей", successCount, totalCount);
        sendResponse(adminBot, adminChatId, response);
    }

    private void sendResponse(AdminBotApplication adminBot, Long chatId, String text) {
        try {
            adminBot.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .build());
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка отправки ответа админу: " + e.getMessage());
        }
    }
}