package com.sparta.restructuring.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter//
@Getter
public class ColumnModifyRequestDto {
    @NotBlank(message = "컬럼 이름은 필수입력입니다.")
    private String columnName;
}
