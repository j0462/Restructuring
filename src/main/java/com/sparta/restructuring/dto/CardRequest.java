package com.sparta.restructuring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CardRequest {
    @NotBlank(message = "카드 이름은 필수 값 입니다.")
    private String title;

    @NotBlank(message = "카드 내용은 필수 값 입니다.")
    private String content;

    @NotBlank(message = "마감 일자는 필수 값 입니다.")
    private LocalDate date;

    @NotBlank(message = "등록할 컬럼의 상태는 필수 값 입니다.")
    private String columnStatus;
}
