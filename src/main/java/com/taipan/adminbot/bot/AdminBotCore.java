package com.taipan.adminbot.bot;

import com.taipan.adminbot.config.AdminBotConfig;
import com.taipan.adminbot.router.AdminUpdateRouter;
import com.taipan.core.bot.BaseBot;
import com.taipan.shared.service.BotManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
public class AdminBotCore extends BaseBot {
    private final AdminBotConfig config;
    private final AdminUpdateRouter updateRouter;

    public AdminBotCore(AdminBotConfig config,
                        AdminUpdateRouter updateRouter,
                        BotManagerService botManagerService) {
        super(
                config.getToken(),
                "admin-bot",
                "ModChecker Admin Bot",
                botManagerService
        );
        this.config = config;
        this.updateRouter = updateRouter;
    }

    @Override
    protected void postRegistration() throws Exception {
        // Регистрируем админ-бота в BotManagerService
        botManagerService.registerBot(getBotId(), this);
        log.debug("🔄 [{}] Зарегистрирован в BotManagerService", getBotId());
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            updateRouter.route(update, this);
        } catch (Exception e) {
            log.error("❌ [{}] Ошибка обработки update: {}", getBotId(), e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }
}