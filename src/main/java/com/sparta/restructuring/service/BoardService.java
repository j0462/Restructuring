package com.sparta.restructuring.service;

import com.sparta.restructuring.dto.BoardRequest;
import com.sparta.restructuring.dto.BoardResponse;
import com.sparta.restructuring.entity.Board;
import com.sparta.restructuring.entity.User;
import com.sparta.restructuring.entity.UserBoard;
import com.sparta.restructuring.entity.UserRole;
import com.sparta.restructuring.exception.board.*;
import com.sparta.restructuring.exception.errorCode.BoardErrorCode;
import com.sparta.restructuring.repository.BoardRepository;
import com.sparta.restructuring.repository.UserBoardRepository;
import com.sparta.restructuring.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final UserBoardRepository userBoardRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository, UserBoardRepository userBoardRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
        this.userBoardRepository = userBoardRepository;
    }

    // 보드 조회
    public List<BoardResponse> getBoard() {
        Iterable<Board> boardIterable = boardRepository.findAll();
        List<Board> boards = StreamSupport.stream(boardIterable.spliterator(), false)
                .collect(Collectors.toList());
        return boards.stream()
                .map(BoardResponse::new)
                .collect(Collectors.toList());
    }

    // 보드 단건 조회
    public BoardResponse getBoardOne(long boardId) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if(boardOptional.isEmpty()){
            throw new IllegalArgumentException("Card를 찾을 수 없습니다");
        }
        Board board = boardOptional.get();
        return new BoardResponse(board);
    }

    // 보드 생성
    @Transactional
    public BoardResponse createBoard(BoardRequest request, User user) {
        validateBoardRequest(request);
        Board newBoard = request.toEntity();
        UserBoard userBoard = new UserBoard(user, newBoard);
        newBoard.getUserBoardList().add(userBoard);
        boardRepository.save(newBoard);
        userBoardRepository.save(userBoard);
        return new BoardResponse(newBoard);
    }

    //보드 수정
    @Transactional
    public BoardResponse updateBoard(Long boardId, BoardRequest request, User user) {
        Board board = getBoardById(boardId);
        validateBoardRequest(request);

        board.setBoardName(request.getBoardName());
        board.setBoardExplain(request.getBoardExplain());
        Optional<UserBoard> optionalUserBoard = userBoardRepository.findByBoardAndUser(board, user);
        if (!optionalUserBoard.isPresent()) {
            // UserBoard가 존재하지 않으면 새로 생성
            UserBoard userBoard = new UserBoard(user, board);
            userBoardRepository.save(userBoard);
        }
        boardRepository.save(board);

        return new BoardResponse(board);
    }

    //보드 삭제
    @Transactional
    public Long deleteBoard(Long boardId, User user) {
        Board board = getBoardById(boardId);

        UserBoard userBoard = new UserBoard(user, board);
        boardRepository.delete(board);
        userBoardRepository.delete(userBoard);
        return board.getBoardId();
    }

    // 보드 초대
    @Transactional
    public BoardResponse inviteUserToBoard(Long boardId, Long userId) {
        String inviterUsername = getCurrentUsername();
        Board board = getBoardById(boardId);

        if (!hasManagerRoleOrIsCreator(inviterUsername, board)) {
            throw new PermissionDeniedException(BoardErrorCode.PERMISSION_DENIED);
        }

        User inviteduser = userRepository.findById(userId).orElse(null);
        validateInvitedUsers(inviteduser);

        if (!isValidUser(inviteduser)) {
            throw new UserNotFoundException(BoardErrorCode.USER_NOT_FOUND);
        }
        if (isUserAlreadyInvited(inviteduser, board)) {
            throw new UserAlreadyInvitedException(BoardErrorCode.USER_ALREADY_INVITED);
        }

        UserBoard userBoard = new UserBoard(inviteduser, board);

        board.getUserBoardList().add(userBoard);
        boardRepository.save(board);
        return new BoardResponse(board);
    }

    // 현재 인증된 사용자의 정보를 가져옵니다.
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    //보드 생성 또는 수정 요청 데이터를 검증합니다.
    private void validateBoardRequest(BoardRequest request) {
        if (request.getBoardName() == null || request.getBoardName().isEmpty() ||
                request.getBoardExplain() == null || request.getBoardExplain().isEmpty()) {
            throw new InvalidBoardDataException(BoardErrorCode.INVALID_BOARD_DATA);
        }
    }

    //ID에 해당하는 보드를 조회하고 반환합니다.
    private Board getBoardById(Long boardId) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        return optionalBoard.orElseThrow(() -> new BoardNotFoundException(BoardErrorCode.BOARD_NOT_FOUND));
    }

    // 초대자가 MANAGER 권한을 가지고 있거나 보드의 생성자인지 확인합니다.
    // `hasManagerRoleOrIsCreator(inviterUsername, board)`와 같은 메서드가 있다고 가정합니다.
    private boolean hasManagerRoleOrIsCreator(String inviterUsername, Board board) {
        User inviter = userRepository.findByAccountId(inviterUsername);
        if (inviter == null) {
            return false;
        }

        return inviter.getRole() == UserRole.MANAGER ||
                board.getUserBoardList().stream().anyMatch(userBoard -> userBoard.getUser().equals(inviter));
    }

    // 초대할 사용자들이 유효한지 확인합니다.
    private void validateInvitedUsers(User user) {
        if (userRepository.findByAccountId(user.getAccountId()) == null) {
            throw new IllegalArgumentException(user.getAccountId() + " 사용자가 존재하지 않습니다.");
        }
    }

    //사용자가 유효한지 검증합니다.
    private boolean isValidUser(User user) {
        return userRepository.findByAccountId(user.getAccountId()) != null;
    }

    // 사용자가 이미 초대된 상태인지 확인하는 메서드
    private boolean isUserAlreadyInvited(User user, Board board) {
        return board.getUserBoardList().stream().anyMatch(userBoard -> userBoard.getUser().equals(user));
    }
}
