package com.sparta.restructuring.repository;

import com.sparta.restructuring.entity.Columns;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Columns, Long> {

    Long countByColumnId(Long boardId);

    Optional<Columns> findByColumnIdAndColumnName(Long boardId, String columnName);

    List<Columns> findAllByBoardBoardIdAndColumnOrderBetween(Long boardId, Long newOrder, Long columnOrder);



    List<Columns> findAllByColumnIdAndColumnOrderGreaterThan(Long id, Long columnOrder);

    Columns findByColumnName(String status);

    List<Columns> findByBoardBoardId(Long boardId);

    @Modifying
    @Query("UPDATE Columns c SET c.columnOrder = c.columnOrder + 1 WHERE c.board.boardId = :boardId AND c.columnOrder BETWEEN :newOrder AND :currentOrder - 1")
    void updateColumnOrder(Long boardId, Long currentOrder, Long newOrder);
}
