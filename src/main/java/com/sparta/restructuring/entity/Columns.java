package com.sparta.restructuring.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Columns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String status;

    @OneToMany(mappedBy = "column")
    private List<Card> cards;
}
