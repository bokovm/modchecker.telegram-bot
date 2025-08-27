package com.taipan.adminbot.router;

import com.taipan.adminbot.AdminBotApplication;
import com.taipan.userbot.bot.BotCore;
import com.taipan.adminbot.command.AdminManageCommand;
import com.taipan.adminbot.command.AdminSendCommand;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class AdminUpdateRouter {
    private final AdminSendCommand adminSendCommand;
    private final AdminManageCommand adminManageCommand;

    public AdminUpdateRouter(AdminSendCommand adminSendCommand,
                             AdminManageCommand adminManageCommand) {
        this.adminSendCommand = adminSendCommand;
        this.adminManageCommand = adminManageCommand;
    }

    public void route(Update update, BotCore adminBot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();

            if (text.startsWith("/sendmessage")) {
                adminSendCommand.handle(update.getMessage(), adminBot);
            } else if (text.startsWith("/admin")) {
                adminManageCommand.handle(update.getMessage(), adminBot);
            } else if (text.startsWith("/help")) {
                sendHelp(adminBot, update.getMessage().getChatId());
            } else {
                sendUnknownCommand(adminBot, update.getMessage().getChatId());
            }
        }
    }

    private void sendHelp(AdminBotApplication adminBot, Long chatId) {
        String helpText = "📚 Команды админ-бота:\n\n" +
                "/sendmessage [@username|all] [bot-name] text - Отправить сообщение\n" +
                "/admin [add|remove] @username - Управление администраторами\n" +
                "/help - Помощь";

        try {
            adminBot.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(helpText)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendUnknownCommand(AdminBotApplication adminBot, Long chatId) {
        try {
            adminBot.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text("❌ Неизвестная команда. Используйте /help для справки.")
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}