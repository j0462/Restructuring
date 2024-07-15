package com.sparta.restructuring.entity;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@Table(name = "card")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Card{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne
    private User user;

    @ManyToOne
    private Columns column;

    @OneToMany(mappedBy = "card")
    private List<Comment> comments;


    @Builder
    public Card(String title, String content, LocalDate date, User user, Columns column) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.user = user;
        this.column = column;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}

