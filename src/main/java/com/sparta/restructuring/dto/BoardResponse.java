package com.sparta.restructuring.dto;

import com.sparta.restructuring.entity.Board;
import lombok.Getter;

@Getter
public class BoardResponse {
    private Long boardId;
    private String boardName;
    private String boardExplain;

    public BoardResponse(Board board) {
        this.boardId = board.getBoardId();
        this.boardName = board.getBoardName();
        this.boardExplain = board.getBoardExplain();
    }

}
