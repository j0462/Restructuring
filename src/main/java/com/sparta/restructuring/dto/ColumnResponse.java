package com.sparta.restructuring.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ColumnResponse {
    private final Long columnId;
    private final String columnName;
    private final Long columnOrder;
    private final Long boardId;

    @Builder
    public ColumnResponse(Long columnId, String columnName, Long columnOrder, Long boardId) {
        this.columnId = columnId;
        this.columnName = columnName;
        this.columnOrder = columnOrder;
        this.boardId = boardId;
    }
}
