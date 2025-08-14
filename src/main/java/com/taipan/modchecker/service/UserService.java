package com.taipan.modchecker.service;

import com.taipan.modchecker.model.UserEntity;
import com.taipan.modchecker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserEntity getOrCreateUser(User tgUser) {
        UserEntity user = userRepository.findByTelegramId(tgUser.getId());
        if (user == null) {
            user = new UserEntity();
            user.setTelegramId(tgUser.getId());
            user.setUsername(tgUser.getUserName());
            user.setFirstName(tgUser.getFirstName());
            user.setLastName(tgUser.getLastName());
            userRepository.save(user);
        }
        return user;
    }
}