package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Сервис для управления регистрацией пользователей.
 */
@Service
public class Register {

    public String registerUser() {
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
