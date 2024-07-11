package com.sparta.restructuring.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CardResponse {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDate date;

    @Builder
    public CardResponse(Long id, String title, String content, LocalDate date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.date = date;
    }
}
