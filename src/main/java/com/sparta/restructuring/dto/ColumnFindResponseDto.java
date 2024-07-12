package com.sparta.restructuring.dto;

import com.sparta.restructuring.entity.Columns;
import lombok.Getter;


@Getter
public class ColumnFindResponseDto {

    private String columnName;
    private Long columnOrder;


    public ColumnFindResponseDto(Columns column) {
        this.columnName = column.getColumnName();
        this.columnOrder = column.getColumnOrder();

    }
}
