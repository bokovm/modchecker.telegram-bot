package com.taipan.adminbot.bot;

import com.taipan.adminbot.config.AdminBotConfig;
import com.taipan.adminbot.router.AdminUpdateRouter;
import com.taipan.shared.service.BotManagerService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class AdminBotCore extends TelegramLongPollingBot {

    private final AdminBotConfig config;
    private final AdminUpdateRouter updateRouter;
    private final BotManagerService botManagerService;
    private final String BOT_ID = "admin-bot";

    public AdminBotCore(AdminBotConfig config,
                        AdminUpdateRouter updateRouter,
                        BotManagerService botManagerService) {
        super(config.getToken());
        this.config = config;
        this.updateRouter = updateRouter;
        this.botManagerService = botManagerService;
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            botManagerService.registerBot(BOT_ID, this);
            System.out.println("✅ Админ бот запущен и зарегистрирован!");
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка регистрации админ-бота: " + e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        updateRouter.route(update, this);
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }
}