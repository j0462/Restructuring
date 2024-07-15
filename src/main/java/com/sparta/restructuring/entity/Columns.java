package com.sparta.restructuring.entity;

import com.sparta.restructuring.base.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Columns extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long columnId;

    @OneToMany(mappedBy = "column")
    private List<Card> cards;

    @Column(nullable = false)
    private String columnName;

    @Column(nullable = false)
    private Long columnOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Builder
    public Columns(String columnName, Board board, Long order) {
        this.columnName = columnName;
        this.board = board;
        this.columnOrder = order;
    }


    public void setColumnOrder(Long columnOrder) {
        this.columnOrder = columnOrder;
    }
}
