package com.taipan.adminbot.service;

import com.taipan.adminbot.model.AdminEntity;
import com.taipan.adminbot.repository.AdminRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public boolean isAdmin(Long telegramId) {
        return adminRepository.existsByTelegramId(telegramId);
    }

    public void addAdmin(User user) {
        if (!isAdmin(user.getId())) {
            AdminEntity admin = new AdminEntity();
            admin.setTelegramId(user.getId());
            admin.setUsername(user.getUserName());
            admin.setFirstName(user.getFirstName());
            admin.setLastName(user.getLastName());
            adminRepository.save(admin);
        }
    }

    public void removeAdmin(Long telegramId) {
        AdminEntity admin = adminRepository.findByTelegramId(telegramId);
        if (admin != null) {
            adminRepository.delete(admin);
        }
    }
}