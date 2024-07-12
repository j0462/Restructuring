package com.sparta.restructuring.repository;


import com.sparta.restructuring.entity.Columns;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ColumnRepository extends JpaRepository<Columns, Long> {

    Long countByBoardId(Long boardId);

    List<Columns> findAllByBoardIdOrderByColumnOrderAsc(Long boardId);

    boolean existsByColumnNameAndBoardId(String columnName, Long boardId);

    Optional<Columns> findByBoardIdAndColumnName(String columnName, Long boardId);

    List<Columns> findAllByBoardIdAndColumnOrderBetween(Long boardId, Long newOrder, Long columnOrder);

    List<Columns> findAllByBoardIdAndColumnOrderGreaterThan(Long id, Long columnOrder);
}
