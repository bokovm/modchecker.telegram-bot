package com.taipan.userbot.bot;

import com.taipan.core.bot.BaseBot;
import com.taipan.shared.service.BotManagerService;
import com.taipan.userbot.config.TelegramBotConfig;
import com.taipan.userbot.router.UpdateRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@Primary
public class BotCore extends BaseBot {
    private final TelegramBotConfig config;
    private final UpdateRouter updateRouter;

    public BotCore(TelegramBotConfig config,
                   UpdateRouter updateRouter,
                   BotManagerService botManagerService) {
        super(
                config.getToken(),
                "user-bot",
                "ModChecker User Bot",
                botManagerService
        );
        this.config = config;
        this.updateRouter = updateRouter;
    }

    // NOTE: UserBot НЕ регистрируется в BotManagerService,
    // поэтому postRegistration() не переопределяем

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