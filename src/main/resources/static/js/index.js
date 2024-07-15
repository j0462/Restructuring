document.addEventListener('DOMContentLoaded', () => {
    const boardsContainer = document.getElementById('boards');
    const createBoardButton = document.getElementById('create-board');
    const editModal = document.getElementById('edit-modal');
    const deleteModal = document.getElementById('delete-modal');
    const closeEditModalButton = document.getElementById('close-edit-modal');
    const closeDeleteModalButton = document.getElementById('close-delete-modal');
    const saveEditButton = document.getElementById('save-edit');
    const confirmDeleteButton = document.getElementById('confirm-delete');
    const cancelDeleteButton = document.getElementById('cancel-delete');
    let currentBoardId = null;

    fetchBoards();

    createBoardButton.addEventListener('click', () => {
        const boardName = prompt('보드 이름:');
        if (boardName) {
            const boardExplain = prompt('보드 설명:');
            if (boardExplain) {
                createBoard(boardName, boardExplain);
            } else {
                alert('보드 설명을 입력해주세요.');
            }
        } else {
            alert('보드 이름을 입력해주세요.');
        }
    });

    closeEditModalButton.addEventListener('click', () => {
        editModal.style.display = 'none';
    });

    closeDeleteModalButton.addEventListener('click', () => {
        deleteModal.style.display = 'none';
    });

    saveEditButton.addEventListener('click', () => {
        const boardName = document.getElementById('edit-board-name').value;
        const boardExplain = document.getElementById('edit-board-explain').value;
        if (boardName && boardExplain) {
            editBoard(currentBoardId, boardName, boardExplain);
            editModal.style.display = 'none';
        } else {
            alert('모든 필드를 입력해주세요.');
        }
    });

    confirmDeleteButton.addEventListener('click', () => {
        deleteBoard(currentBoardId);
        deleteModal.style.display = 'none';
    });

    cancelDeleteButton.addEventListener('click', () => {
        deleteModal.style.display = 'none';
    });

    function fetchBoards() {
        fetch('/api/board', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}` // 인증 토큰 추가
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP 에러! ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                if (data.statusCode === 200) {
                    displayBoards(data.data);
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('보드 가져오기 실패:', error);
                alert('보드 가져오기 실패');
            });
    }

    function displayBoards(boards) {
        boardsContainer.innerHTML = '';
        boards.forEach(board => {
            const boardElement = document.createElement('div');
            boardElement.classList.add('board');
            boardElement.innerHTML = `
                <div class="board-title">${board.boardName}</div>
                <div class="board-buttons">
                    <button class="edit-button" data-board-id="${board.boardId}" data-board-name="${board.boardName}" data-board-explain="${board.boardExplain}">수정</button>
                    <button class="delete-button" data-board-id="${board.boardId}">삭제</button>
                </div>
            `;
            boardElement.addEventListener('click', (event) => {
                if (!event.target.classList.contains('edit-button') && !event.target.classList.contains('delete-button')) {
                    window.location.href = `/home?boardId=${board.boardId}`;
                }
            });
            boardsContainer.appendChild(boardElement);
        });

        document.querySelectorAll('.edit-button').forEach(button => {
            button.addEventListener('click', (event) => {
                event.stopPropagation();
                const boardId = button.getAttribute('data-board-id');
                const boardName = button.getAttribute('data-board-name');
                const boardExplain = button.getAttribute('data-board-explain');
                showEditModal(boardId, boardName, boardExplain);
            });
        });

        document.querySelectorAll('.delete-button').forEach(button => {
            button.addEventListener('click', (event) => {
                event.stopPropagation();
                const boardId = button.getAttribute('data-board-id');
                showDeleteModal(boardId);
            });
        });
    }

    function showEditModal(boardId, name, explanation) {
        currentBoardId = boardId;
        document.getElementById('edit-board-name').value = name;
        document.getElementById('edit-board-explain').value = explanation;
        editModal.style.display = 'block';
    }

    function showDeleteModal(boardId) {
        currentBoardId = boardId;
        deleteModal.style.display = 'block';
    }

    function createBoard(name, explanation) {
        fetch('/api/board', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
            body: JSON.stringify({ boardName: name, boardExplain: explanation }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                if (data.statusCode === 200) {
                    fetchBoards();
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('보드 생성 실패:', error);
                alert('보드 생성 실패');
            });
    }

    function editBoard(boardId, name, explanation) {
        fetch(`/api/board/${boardId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
            body: JSON.stringify({ boardName: name, boardExplain: explanation }),
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                if (data.statusCode === 200) {
                    fetchBoards();
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('보드 수정 실패:', error);
                alert('보드 수정 실패');
            });
    }

    function deleteBoard(boardId) {
        fetch(`/api/board/${boardId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                if (data.statusCode === 200) {
                    fetchBoards();
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('보드 삭제 실패:', error);
                alert('보드 삭제 실패');
            });
    }
});
