package com.taipan.userbot.service;

import com.taipan.userbot.command.BotCommand;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BotService {

    private final Map<String, BotCommand> commands;
    private final BotCommand unknownCommand;

    public BotService(List<BotCommand> commandsList, BotCommand unknownCommand) {
        // Собираем команды в Map для быстрого поиска по имени команды
        this.commands = commandsList.stream()
                .collect(Collectors.toMap(BotCommand::getCommandName, Function.identity()));
        this.unknownCommand = unknownCommand;
    }

    public SendMessage processCommand(String input, long chatId) {
        String command = input.split(" ")[0]; // Берем первое слово
        return commands.getOrDefault(command, unknownCommand)
                .execute(chatId);
    }
}
