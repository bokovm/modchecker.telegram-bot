package com.taipan.shared.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BotManagerService {
    private final Map<String, TelegramLongPollingBot> bots = new ConcurrentHashMap<>();

    public void registerBot(String botId, TelegramLongPollingBot bot) {
        bots.put(botId, bot);
        log.info("Registered bot: {} - {} ", botId, bot.getBotUsername());
    }

    public boolean sendAsBot(String botId, long chatId, String text) {
        TelegramLongPollingBot bot = bots.get(botId);
        if (bot == null) {
            log.error("Bot not found: {}", botId);
            return false;
        }

        try {
            bot.execute(SendMessage.builder()
                    .chatId(String.valueOf(chatId))
                    .text(text)
                    .build());
            return true;
        } catch (TelegramApiException e) {
            log.error("Error sending via {}: {}", botId, e.getMessage(), e);
            return false;
        }
    }

    public String getRegisteredBotsList() {
        return bots.entrySet().stream()
                .map(entry -> "• " + entry.getKey() + " - " + entry.getValue().getBotUsername())
                .collect(Collectors.joining("\n"));
    }

    public Map<String, TelegramLongPollingBot> getRegisteredBots() {
        return new ConcurrentHashMap<>(bots);
    }
}