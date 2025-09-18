package com.taipan.adminbot.service;

import com.taipan.adminbot.command.AdminCommand;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AdminBotService {

    private final Map<String, AdminCommand> commands;
    private final AdminCommand unknownCommand;

    public AdminBotService(List<AdminCommand> commandsList,
                           @Qualifier("adminUnknownCommand") AdminCommand unknownCommand) {
        this.commands = commandsList.stream()
                .collect(Collectors.toMap(AdminCommand::getCommandName, Function.identity()));
        this.unknownCommand = unknownCommand;
    }

    public void processCommand(String input, Message message, AbsSender bot) {
        String command = input.split(" ")[0];
        commands.getOrDefault(command, unknownCommand)
                .execute(message, bot);
    }
}
