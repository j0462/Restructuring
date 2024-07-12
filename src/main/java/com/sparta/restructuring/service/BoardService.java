package com.sparta.restructuring.service;

import com.sparta.restructuring.dto.BoardRequest;
import com.sparta.restructuring.dto.BoardResponse;
import com.sparta.restructuring.entity.Board;
import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.repository.BoardRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public List<BoardResponse> getBoard() {
        Iterable<Board> boardIterable = boardRepository.findAll();
        List<Board> board = StreamSupport.stream(boardIterable.spliterator(), false)
                .collect(Collectors.toList());
        List<BoardResponse> response = board.stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());
        return response;
    }

    @Transactional
    public BoardResponse createBoard(BoardRequest request) {
        validateBoardRequest(request);
        Board newBoard = request.toEntity();
        boardRepository.save(newBoard);
        return new BoardResponse(newBoard);
    }

    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest request) {
        Board board = getBoardById(boardId);
        validateBoardRequest(request);

        board.setBoardName(request.getBoardName());
        board.setBoardExplain(request.getBoardExplain());
        boardRepository.save(board);
        return new BoardResponse(board);
    }

    @Transactional
    public Long deleteBoard(Long boardId, String boardName) {
        Board board = getBoardById(boardId);

        if (!board.getBoardName().equals(boardName)) {
            throw new IllegalArgumentException("삭제하려는 보드 이름과 일치하지 않습니다.");
        }

        boardRepository.delete(board);
        return board.getBoardId();
    }

    private void validateBoardRequest(BoardRequest request) {
        if (request.getBoardName() == null || request.getBoardName().isEmpty() ||
                request.getBoardExplain() == null || request.getBoardExplain().isEmpty()) {
            throw new IllegalArgumentException("보드 이름과 한 줄 설명은 필수입니다.");
        }
    }

    private Board getBoardById(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isEmpty()) {
            throw new IllegalArgumentException("해당 ID의 보드를 찾을 수 없습니다.");
        }
        return optionalBoard.get();
    }

    @Transactional
    public BoardResponse inviteUserToBoard(Long boardId, List<String> invitedUsers) {
        // 현재 인증된 사용자의 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String inviterUsername = authentication.getName(); // 현재 사용자의 이름을 가져옵니다.

        Board board = getBoardById(boardId);

        // 초대자가 MANAGER 권한을 가지고 있거나 보드의 생성자인지 확인합니다.
        // `hasManagerRoleOrIsCreator(inviterUsername, board)`와 같은 메서드가 있다고 가정합니다.
        if (!hasManagerRoleOrIsCreator(inviterUsername, board)) {
            throw new IllegalArgumentException("권한이 없습니다. MANAGER 권한이 필요합니다.");
        }

        // 초대할 사용자들이 유효한지 확인합니다.
        validateInvitedUsers(invitedUsers);

        // 초대할 사용자들을 보드 엔터티에 추가합니다.
        for (String username : invitedUsers) {
            // `isValidUser(username)`와 같은 메서드가 있다고 가정합니다.
            if (!isValidUser(username)) {
                throw new IllegalArgumentException(username + " 사용자가 존재하지 않습니다.");
            }
            // `isUserAlreadyInvited(username, board)`와 같은 메서드가 있다고 가정합니다.
            if (isUserAlreadyInvited(username, board)) {
                throw new IllegalArgumentException(username + " 사용자는 이미 초대되었습니다.");
            }
            board.addInvitedUser(username);
        }

        // 업데이트된 보드 엔터티를 저장합니다.
        boardRepository.save(board);
        return new BoardResponse(board);
    }

    // 초대자가 MANAGER 권한을 가지거나 보드의 생성자인지 확인하는 메서드
    private boolean hasManagerRoleOrIsCreator(String inviterUsername, Board board) {
        // 실제 애플리케이션의 보안 및 비즈니스 로직에 따라 구현합니다.
        return true; // 실제 구현으로 대체해야 합니다.
    }

    // 초대할 사용자들이 유효한지 확인하는 메서드
    private void validateInvitedUsers(List<String> invitedUsers) {
        // 실제 애플리케이션의 요구사항에 따라 구현합니다.
        // 필요에 따라 유효성 검사 로직을 추가합니다.
    }

    // 사용자가 유효한지 확인하는 메서드
    private boolean isValidUser(String username) {
        // 실제 애플리케이션의 사용자 검증 로직에 따라 구현합니다.
        return true; // 실제 구현으로 대체해야 합니다.
    }

    // 사용자가 이미 초대된 상태인지 확인하는 메서드
    private boolean isUserAlreadyInvited(String username, Board board) {
        // 실제 애플리케이션의 초대 상태 검사 로직에 따라 구현합니다.
        return false; // 실제 구현으로 대체해야 합니다.
    }

}
