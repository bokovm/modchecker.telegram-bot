package com.taipan.userbot.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@Configuration
public class HandlerMessageConfig {

    @Bean("callbackMessages")
    public MessageSource callbackMessages() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("UserBotHandlerMessages/callback_query");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean("commandMessages")
    public MessageSource commandMessages() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("UserBotHandlerMessages/command_handler.properties");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean("documentMessages")
    public MessageSource documentMessages() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("UserBotHandlerMessages/document_handler");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
