package com.example.demo.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * "Репозиторий" для хранения пользователей.
 */
@Component
@AllArgsConstructor
public class UserRepo {
    private final List<String> users = new ArrayList<>();

    public void addUser(String uuid) {
        users.add(uuid);
    }

    public boolean userExists(String uuid) {
        return users.contains(uuid);
    }
}
