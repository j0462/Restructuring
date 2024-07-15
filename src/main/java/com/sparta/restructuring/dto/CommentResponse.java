package com.sparta.restructuring.dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {
    private final Long id;
    private final String content;
    private final LocalDate date;

    @Builder
    public CommentResponse(Long id, String content, LocalDate date) {
        this.id = id;
        this.content = content;
        this.date = date;
    }
}
