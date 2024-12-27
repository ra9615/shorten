package com.example.demo.service;

import com.example.demo.domain.Url;
import com.example.demo.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис для работы со ссылками.
 */
@Service
@RequiredArgsConstructor
public class ShortUrl {
    private final Map<String, Url> urlStore = new ConcurrentHashMap<>();
    private final Map<String, List<String>> userUrls = new ConcurrentHashMap<>();

    private final UserRepo userRepo;

    @Value("${short.url.click.limit}")
    private Integer clickLimit;

    @Value("${short.url.live.time}")
    private Integer lifeLimit;

    /**
     * Метод предназначен для сокращения ссылок.
     */
    public String shorten(String originalUrl, String userId, Integer maxClicks, Integer maxDays) {
        validUserExists(userId);

        String shortUrl = UUID.randomUUID().toString().substring(0, 8);
        Url url = new Url(
                originalUrl,
                userId,
                Objects.isNull(maxClicks) ? clickLimit : chooseClickLimit(maxClicks),
                Objects.isNull(maxDays) ? getExpireDate(lifeLimit) : chooseLiveTime(maxDays)
        );
        urlStore.put(shortUrl, url);
        userUrls.computeIfAbsent(userId, k -> new ArrayList<>()).add(shortUrl);
        return shortUrl;
    }

    /**
     * Метод предназначен для открытия коротких ссылок в браузере.
     */
    public void goToSite(String userId, String shortUrl) throws Exception {
        validUserExists(userId);
        Url url = urlStore.get(shortUrl);
        validUrl(userId, url, shortUrl);
        url.decrementClicks();
        openUrlInBrowser(url.getOriginalUrl());
    }

    /**
     * Метод предназначен для удаления ссылок.
     */
    public String deleteShortUrl(String userId, String shortUrl) {
        List<String> urls = userUrls.get(userId);
        validUserExists(userId);
        validUrlExists(urls, shortUrl);
        urls.remove(shortUrl);
        urlStore.remove(shortUrl);
        return String.format("Ссылка %s успешно удалена", shortUrl);
    }

    /**
     * Метод предназначен для редактирования лимитов переходов по ссылке.
     */
    public String editLimits(String userId, String shortUrl, int max) {
        validUserExists(userId);
        validUrlExists(userUrls.get(userId), shortUrl);
        urlStore.get(shortUrl).setRemainingClicks(max);
        return String.format("Для лимита переходов по ссылке %s  установлено значение %d", shortUrl, max);

    }

    private void openUrlInBrowser(String url) throws Exception {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            // Windows
            Runtime.getRuntime().exec(new String[]{"cmd", "/c", "start", url});
        } else if (os.contains("mac")) {
            // macOS
            Runtime.getRuntime().exec(new String[]{"open", url});
        } else if (os.contains("nix") || os.contains("nux")) {
            // Linux
            Runtime.getRuntime().exec(new String[]{"xdg-open", url});
        } else {
            throw new UnsupportedOperationException("Не удалось определить систему.");
        }
    }

    private LocalDateTime chooseLiveTime(Integer maxDays) {
        Integer result = maxDays > lifeLimit ? lifeLimit : maxDays;
        return LocalDateTime.now().plusDays(result);
    }

    private Integer chooseClickLimit(Integer maxClicks) {
        return maxClicks > clickLimit ? maxClicks : clickLimit;
    }

    private void validUserExists(String userId) {
        if (!userRepo.userExists(userId))
            throw new RuntimeException(String.format("Пользователь %s не существует. Необходимо зарегистрироваться", userId));
    }

    private void validUrlExists(List<String> urls, String shortUrl) {
        if (!urls.contains(shortUrl))
            throw new RuntimeException(String.format("Ссылки %s у Вас не существует.", shortUrl));

    }

    private LocalDateTime getExpireDate(Integer lifeLimit) {
        return LocalDateTime.now().plusDays(lifeLimit);
    }

    private void validUrl(String userId, Url url, String shortUrl) {
        if (Objects.isNull(url))
            throw new RuntimeException(String.format("Ссылка %s не существует.", shortUrl));

        if (url.getExpiryTime().isBefore(LocalDateTime.now())) {
            deleteShortUrl(userId, shortUrl);
            throw new RuntimeException(String.format("У ссылки %s истек срок действия. Она была удалена.", shortUrl));
        }

        if (url.getRemainingClicks() <= 0) {
            deleteShortUrl(userId, shortUrl);
            throw new RuntimeException(String.format("Лимит переходов по ссылке %s исчерпан. Она была удалена.", shortUrl));
        }
    }
}
