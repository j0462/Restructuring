package com.sparta.restructuring.service;

import com.sparta.restructuring.dto.ColumnCreateRequestDto;
import com.sparta.restructuring.dto.ColumnFindRequestDto;
import com.sparta.restructuring.dto.ColumnFindResponseDto;
import com.sparta.restructuring.dto.ColumnModifyRequestDto;
import com.sparta.restructuring.entity.Board;
import com.sparta.restructuring.exception.column.BoardNotFoundException;
import com.sparta.restructuring.exception.column.ColumnDuplicatedException;
import com.sparta.restructuring.exception.column.ColumnNotFoundException;
import com.sparta.restructuring.exception.column.InvalidOrderException;
import com.sparta.restructuring.exception.errorCode.ColumnErrorCode;
import com.sparta.restructuring.repository.BoardRepository;
import com.sparta.restructuring.repository.ColumnRepository;
import lombok.RequiredArgsConstructor;
import com.sparta.restructuring.entity.Columns;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;

    public void createColumn(ColumnCreateRequestDto requestDto) {
        Board board = boardRepository.findById(requestDto.getBoardId())
                .orElseThrow(() -> new BoardNotFoundException(
                        ColumnErrorCode.BOARD_NOT_FOUND));
        if (columnRepository.existsByColumnNameAndBoardId(requestDto.getColumnName(),
                requestDto.getBoardId())) {
            throw new ColumnDuplicatedException(ColumnErrorCode.COLUMN_ALREADY_REGISTERED_ERROR);
        }

        Long columnOrder = columnRepository.countByBoardId(requestDto.getBoardId());
        Columns columns = Columns.builder().board(board).columnName(requestDto.getColumnName())
                .order(columnOrder).build();
        columnRepository.save(columns);
    }


    @Transactional
    public void deleteColumn(Long columnId) {

        Columns columns = columnRepository.findById(columnId)
                .orElseThrow(() -> new ColumnNotFoundException(ColumnErrorCode.COLUMN_NOT_FOUND));
        columnRepository.deleteById(columnId);
        List<Columns> columnsList = columnRepository.findAllByBoardIdAndColumnOrderGreaterThan(
                columns.getBoard().getId(), columns.getColumnOrder());
        for (Columns column : columnsList) {
            column.setColumnOrder(column.getColumnOrder() - 1);
        }
    }


    @Transactional
    public void modifyColumnOrder(Long columnId, Long newOrder) {

        Columns columns = columnRepository.findById(columnId)
                .orElseThrow(() -> new ColumnNotFoundException(ColumnErrorCode.COLUMN_NOT_FOUND));

        Long boardId = columns.getBoard().getId();
        Long maxOrder = columnRepository.countByBoardId(boardId) - 1;

        if (newOrder < 0 || newOrder > maxOrder) {
            throw new InvalidOrderException(ColumnErrorCode.INVALID_ORDER);
        }

        if (Objects.equals(columns.getColumnOrder(), newOrder)) {
            throw new InvalidOrderException(ColumnErrorCode.INVALID_ORDER);
        } else if (columns.getColumnOrder() > newOrder) {
            List<Columns> columnsList = columnRepository.findAllByBoardIdAndColumnOrderBetween(
                    boardId, newOrder, columns.getColumnOrder());
            for (Columns column : columnsList) {
                if (Objects.equals(column.getColumnId(), columns.getColumnId())) {
                    columns.setColumnOrder(newOrder);
                    continue;
                }
                column.setColumnOrder(column.getColumnOrder() + 1);
            }
        } else {
            List<Columns> columnsList = columnRepository.findAllByBoardIdAndColumnOrderBetween(
                    boardId, columns.getColumnOrder(), newOrder);
            for (Columns column : columnsList) {
                if (column.equals(columns)) {
                    columns.setColumnOrder(newOrder);
                    continue;
                }
                column.setColumnOrder(column.getColumnOrder() - 1);
            }
        }

    }
}
