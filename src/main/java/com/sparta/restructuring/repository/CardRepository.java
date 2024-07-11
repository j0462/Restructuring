package com.sparta.restructuring.repository;

import com.sparta.restructuring.entity.Card;
import com.sparta.restructuring.entity.ColumnStatus;
import com.sparta.restructuring.entity.Columns;
import com.sparta.restructuring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findBycolumn(Columns coloumn);
    List<Card> findByUser(User user);
}
