package com.sparta.restructuring.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentResponse {
    private final Long id;
    private final String content;

    @Builder
    public CommentResponse(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
