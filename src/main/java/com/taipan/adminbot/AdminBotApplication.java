package com.taipan.adminbot;

import com.taipan.adminbot.command.AdminSendCommand;
import com.taipan.adminbot.config.AdminBotConfig;
import com.taipan.shared.service.BotManagerService;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class AdminBotApplication extends TelegramLongPollingBot {
    private final AdminBotConfig config;
    private final AdminSendCommand sendCommand;
    private final String BOT_ID = "admin-bot";

    public AdminBotApplication(AdminBotConfig config,
                               AdminSendCommand sendCommand,
                               BotManagerService botManager) {
        super(config.getToken());
        this.config = config;
        this.sendCommand = sendCommand;
        botManager.registerBot(BOT_ID, this);
    }

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("✅ Админ бот запущен и зарегистрирован в Telegram API!");
        } catch (TelegramApiException e) {
            System.err.println("❌ Ошибка регистрации админ-бота: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Получено обновление в админ-боте: " + update);

        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            System.out.println("Текст сообщения: " + text);

            if (text.startsWith("/sendmessage")) {
                sendCommand.handle(update.getMessage(), this);
            } else if (text.startsWith("/help")) {
                sendHelp(update.getMessage().getChatId());
            } else if (text.startsWith("/bots")) {
                listBots(update.getMessage().getChatId());
            }
        }
    }

    private void sendHelp(Long chatId) {
        String helpText = "📚 Команды админ-бота:\n\n" +
                "/sendmessage [@username|all] [bot-name] text - Отправить сообщение\n" +
                "/bots - Список зарегистрированных ботов\n" +
                "/help - Помощь";

        try {
            execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(helpText)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listBots(Long chatId) {
        // Этот метод нужно доработать, чтобы получать список ботов из BotManagerService
        String botsList = "🤖 Зарегистрированные боты:\n\n" +
                "• user-bot - Основной бот для пользователей\n" +
                "• admin-bot - Админский бот";

        try {
            execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(botsList)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}