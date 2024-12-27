package com.example.demo.service;

import com.example.demo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * Сервис для работы со ссылками в консоли.
 */
@ShellComponent
@RequiredArgsConstructor
public class UrlShell {


    private final Register register;
    private final UserRepo userRepo;
    private final ShortUrl shortUrl;

    @ShellMethod("Регистрация нового пользователя. Пример: register")
    public String register() {
        String uuid = register.registerUser();
        userRepo.addUser(uuid);
        return String.format("Ваш уникальный идентификатор: %s", uuid);
    }

    @ShellMethod("Укоротить ссылку. Пример: shorten originalUrl userId (опционально)maxClicks (опционально)maxDays")
    public String shorten(String originalUrl, String userId, @ShellOption Integer maxClicks, @ShellOption Integer maxDays) {
        return String.format("Ваша ссылка в коротком виде: %s", shortUrl.shorten(originalUrl, userId, maxClicks, maxDays));
    }

    @ShellMethod("Перейти по короткой ссылке. Пример: goSite userId shortUrl")
    public void go(String userId, String url) throws Exception {
        shortUrl.goToSite(userId, url);
    }

    @ShellMethod("Удалить короткую ссылку. Пример: delete userId shortUrl")
    public String delete(String userId, String url) {
        return shortUrl.deleteShortUrl(userId, url);
    }

    @ShellMethod("Установить лимит переходов по ссылке. Пример: userId shortUrl clickLimit")
    public String edit(String userId, String url, int clickLimit) {
        return shortUrl.editLimits(userId, url, clickLimit);
    }
}
