package com.taipan.adminbot.router;

import com.taipan.adminbot.service.AdminBotService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class AdminUpdateRouter {
    private final AdminBotService adminBotService;

    public AdminUpdateRouter(AdminBotService adminBotService) {
        this.adminBotService = adminBotService;
    }

    public void route(Update update, AbsSender bot) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            adminBotService.processCommand(text, update.getMessage(), bot);
        }
    }
}