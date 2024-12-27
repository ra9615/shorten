package com.example.demo.domain;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Сущность ссылки.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Url {
    /**
     * Оригинальная ссылка.
     */
    private String originalUrl;

    /**
     * "Владелец ссылки.
     */
    private String userId;

    /**
     * Оставшееся кол-во переходов по ссылке.
     */
    private int remainingClicks;

    /**
     * Дата истечения срока действия ссылки.
     */
    private LocalDateTime expiryTime;

    public void decrementClicks() {
        this.remainingClicks--;
    }
}
