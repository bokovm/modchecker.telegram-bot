package com.taipan.adminbot.command;

import com.taipan.adminbot.service.AdminSendService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class AdminSendCommand implements AdminCommand {

    private final AdminSendService adminSendService;

    public AdminSendCommand(AdminSendService adminSendService) {
        this.adminSendService = adminSendService;
    }

    @Override
    public String getCommandName() {
        return "/sendmessage";
    }

    @Override
    public void execute(Message message, AbsSender bot) {
        Long senderId = message.getFrom().getId();

        if (!adminSendService.isAdmin(senderId)) {
            sendResponse(bot, message.getChatId(), "❌ Недостаточно прав. Только администраторы могут использовать эту команду.");
            return;
        }

        String[] args = message.getText().split(" ", 4);
        if (args.length < 4) {
            sendResponse(bot, message.getChatId(), "❌ Формат: /sendmessage [@username|all] [bot-name] text");
            return;
        }

        String target = args[1];
        String botName = args[2];
        String text = args[3];

        String response;
        if ("all".equalsIgnoreCase(target)) {
            response = adminSendService.broadcast(botName, text);
        } else {
            response = adminSendService.sendToUser(target, botName, text);
        }

        sendResponse(bot, message.getChatId(), response);
    }

    private void sendResponse(AbsSender bot, Long chatId, String text) {
        SendMessage response = SendMessage.builder()
                .chatId(chatId.toString())
                .text(text)
                .build();
        try {
            bot.execute(response);
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка отправки ответа админу: " + e.getMessage());
        }
    }
}
