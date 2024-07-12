package com.sparta.restructuring.dto;

import com.sparta.restructuring.entity.Board;
import com.sparta.restructuring.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.util.List;

@Getter
public class BoardRequest {
    @NotBlank
    private String boardName;

    @NotBlank
    private String boardExplain;

    private List<User> invitedUsers;

    public Board toEntity() {
        return Board.builder()
                .boardName(boardName)
                .boardExplain(boardExplain)
                .build();
    }
}
