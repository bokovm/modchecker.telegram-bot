package com.taipan.adminbot.service;

import com.taipan.adminbot.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminManageService {

    private final AdminRepository adminRepository;

    public AdminManageService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public boolean isAdmin(Long telegramId) {
        return adminRepository.existsByTelegramId(telegramId);
    }

    public String addAdmin(String username) {
        String cleanUsername = cleanUsername(username);
        // Тут добавляем запись в базу, если нужно
        // adminRepository.save(new AdminEntity(cleanUsername, ...));

        return String.format("✅ Администратор @%s успешно добавлен.", cleanUsername);
    }

    public String removeAdmin(String username) {
        String cleanUsername = cleanUsername(username);
        // Тут удаляем запись из базы
        // adminRepository.deleteByUsername(cleanUsername);

        return String.format("✅ Администратор @%s удалён.", cleanUsername);
    }

    private String cleanUsername(String username) {
        return username.startsWith("@") ? username.substring(1) : username;
    }
}
