package com.sparta.restructuring.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    private String boardName;

    private String boardExplain;

    private List<String> invitedUsers = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserBoard> userBoardList = new ArrayList<>();

    @Builder
    public Board(String boardName, String boardExplain) {
        this.boardName = boardName;
        this.boardExplain = boardExplain;
    }

    public void setBoardName(String boardName) {
        this.boardName = boardName;
    }

    public void setBoardExplain(String boardExplain) {
        this.boardExplain = boardExplain;
    }

    public void addInvitedUser(String username) {
        invitedUsers.add(username);
    }
}
