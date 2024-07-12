package com.sparta.restructuring.controller;

import com.sparta.restructuring.base.CommonResponse;
import com.sparta.restructuring.dto.BoardRequest;
import com.sparta.restructuring.dto.BoardResponse;
import com.sparta.restructuring.entity.Board;
import com.sparta.restructuring.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.PortUnreachableException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequestMapping("/board")
@RestController
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 보드 목록 조회
    @GetMapping
    public ResponseEntity<List<BoardResponse>> getBoard() {
        Iterable<Board> boardIterable = boardService.getBoard();
        List<Board> board = StreamSupport.stream(boardIterable.spliterator(), false)
                .collect(Collectors.toList());
        List<BoardResponse> response = board.stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok().body(response);
    }

    // 보드 생성
    @PostMapping
    public ResponseEntity postBoard(@RequestBody BoardRequest request) {
        Board board = boardService.createBoard(request);
        BoardResponse response = new BoardResponse(board);
        return ResponseEntity.ok().body(response);
    }

    // 보드 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<BoardResponse> putBoard(@PathVariable Long boardId, @RequestBody BoardRequest request) {
        Board board = boardService.updateBoard(boardId, request);
        BoardResponse response = new BoardResponse((board));
        return ResponseEntity.ok().body(response);
    }

    // 보드 삭제
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long boardId, @RequestBody BoardRequest request) {
        boardService.deleteBoard(boardId, request.getBoardName());
        return ResponseEntity.ok().build();
    }

    // 보드 초대
    @PostMapping("/{boardId}/invite")
    public ResponseEntity<BoardResponse> inviteUserToBoard(@PathVariable Long boardId,
                                                           @RequestBody BoardRequest request) {
        // 현재 인증된 사용자의 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String inviterUsername = authentication.getName(); // 현재 사용자의 이름을 가져옵니다.

        // boardService를 사용하여 보드에 사용자를 초대합니다.
        Board board = boardService.inviteUserToBoard(boardId, request.getInvitedUsers(), inviterUsername);

        // 초대 후 업데이트된 보드 정보를 반환합니다.
        BoardResponse response = new BoardResponse(board);
        return ResponseEntity.ok().body(response);
    }


}
