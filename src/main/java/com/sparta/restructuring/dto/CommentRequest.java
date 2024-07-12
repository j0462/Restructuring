package com.sparta.restructuring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequest {
    @NotBlank(message = "댓글 내용은 필수 값 입니다.")
    private String content;
}
