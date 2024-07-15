
package com.sparta.restructuring.service;

import com.sparta.restructuring.dto.CardResponse;
import com.sparta.restructuring.dto.ColumnCreateRequestDto;
import com.sparta.restructuring.dto.ColumnResponse;
import com.sparta.restructuring.dto.ColumnUpdateRequest;
import com.sparta.restructuring.entity.Board;
import com.sparta.restructuring.entity.Card;
import com.sparta.restructuring.entity.Columns;
import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.entity.UserBoard;
import com.sparta.restructuring.exception.column.BoardNotFoundException;
import com.sparta.restructuring.exception.column.ColumnDuplicatedException;
import com.sparta.restructuring.exception.column.ColumnNotFoundException;
import com.sparta.restructuring.exception.column.InvalidOrderException;
import com.sparta.restructuring.exception.errorCode.ColumnErrorCode;
import com.sparta.restructuring.repository.BoardRepository;
import com.sparta.restructuring.repository.ColumnRepository;
import com.sparta.restructuring.repository.UserBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;
    private final UserBoardRepository userBoardRepository;

    public ColumnResponse createColumn(ColumnCreateRequestDto requestDto, Long boardId, User loginUser) {
        Board existboard = boardRepository.findById(boardId).orElse(null);
        Optional<UserBoard> userBoard = userBoardRepository.findByBoardAndUser(existboard, loginUser);
        if (userBoard.isEmpty()) {
            throw new ColumnDuplicatedException(ColumnErrorCode.NOT_INVITED_USER);
        }

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new BoardNotFoundException(
                        ColumnErrorCode.BOARD_NOT_FOUND));
        if (columnRepository.findByColumnIdAndColumnName(boardId, requestDto.getColumnName()).isPresent()) {
            throw new ColumnDuplicatedException(ColumnErrorCode.COLUMN_ALREADY_REGISTERED_ERROR);
        }

        Long columnOrder = columnRepository.countByColumnId(boardId);
        Columns columns = Columns.builder().board(board).columnName(requestDto.getColumnName())
                .order(columnOrder +1).build();
        columnRepository.save(columns);
        return new ColumnResponse(columns.getColumnId(), columns.getColumnName(), columns.getColumnOrder(), board.getBoardId());
    }


    @Transactional
    public Long deleteColumn(Long columnId, Long boardId, User loginUser) {
        Board existboard = boardRepository.findById(boardId).orElse(null);
        Optional<UserBoard> userBoard = userBoardRepository.findByBoardAndUser(existboard, loginUser);
        if (userBoard.isEmpty()) {
            throw new ColumnDuplicatedException(ColumnErrorCode.NOT_INVITED_USER);
        }

        Columns columns = columnRepository.findById(columnId)
                .orElseThrow(() -> new ColumnNotFoundException(ColumnErrorCode.COLUMN_NOT_FOUND));
        columnRepository.deleteById(columnId);
        List<Columns> columnsList = columnRepository.findAllByColumnIdAndColumnOrderGreaterThan(
                columns.getBoard().getBoardId(), columns.getColumnOrder());
        for (Columns column : columnsList) {
            column.setColumnOrder(column.getColumnOrder() - 1);
        }
        return columns.getColumnId();
    }


    @Transactional
    public ColumnResponse modifyColumnOrder(Long columnId, Long newOrder, User loginUser) {
        Columns columns = columnRepository.findById(columnId)
            .orElseThrow(() -> new ColumnNotFoundException(ColumnErrorCode.COLUMN_NOT_FOUND));

        Long boardId = columns.getBoard().getBoardId();
        Board existboard = boardRepository.findById(boardId).orElse(null);
        Optional<UserBoard> userBoard = userBoardRepository.findByBoardAndUser(existboard, loginUser);
        if (userBoard.isEmpty()) {
            throw new ColumnDuplicatedException(ColumnErrorCode.NOT_INVITED_USER);
        }

        if (newOrder < 0) {
            throw new InvalidOrderException(ColumnErrorCode.INVALID_ORDER);
        }

        if (Objects.equals(columns.getColumnOrder(), newOrder)) {
            throw new InvalidOrderException(ColumnErrorCode.INVALID_ORDER);
        }

        // 잠금 순서 조정
        columnRepository.updateColumnOrder(boardId, columns.getColumnOrder(), newOrder);
        columns.setColumnOrder(newOrder);
        columnRepository.save(columns);

        return new ColumnResponse(columns.getColumnId(), columns.getColumnName(), columns.getColumnOrder(), boardId);
    }

    public List<ColumnResponse> getAllColumns(Long boardId, User loginUser) {
        Board existboard = boardRepository.findById(boardId).orElse(null);
        Optional<UserBoard> userBoard = userBoardRepository.findByBoardAndUser(existboard, loginUser);
        if (userBoard.isEmpty()) {
            throw new ColumnDuplicatedException(ColumnErrorCode.NOT_INVITED_USER);
        }

        List<Columns> columnsList = columnRepository.findByBoardBoardId(boardId);
        List<ColumnResponse> columnResponselist = new ArrayList<>();

        for (Columns column : columnsList) {
            ColumnResponse columnResponseDto = new ColumnResponse(column.getColumnId(), column.getColumnName(), column.getColumnOrder(), boardId);
            columnResponselist.add(columnResponseDto);
        }

        return columnResponselist;
    }

    public ColumnResponse updateColumn(Long columnId, ColumnUpdateRequest request, User user) {
        Columns column = columnRepository.findById(columnId).orElseThrow(() -> new IllegalArgumentException("Column을 찾을 수 없습니다."));

        column.updateColumnName(request.getColumnName());
        columnRepository.save(column);

        return ColumnResponse.builder()
            .columnId(column.getColumnId())
            .columnName(column.getColumnName())
            .columnOrder(column.getColumnOrder())
            .boardId(column.getBoard().getBoardId())
            .build();
    }
}
