package com.sparta.restructuring.repository;

import com.sparta.restructuring.entity.Columns;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Columns, Long> {
    Columns findByStatus(String status);
}
