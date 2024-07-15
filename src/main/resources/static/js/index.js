document.addEventListener('DOMContentLoaded', () => {
    const boardsContainer = document.getElementById('boards');
    const createBoardButton = document.getElementById('create-board');

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
            `;
            boardElement.addEventListener('click', () => {
                window.location.href = `/home?boardId=${board.boardId}`;
            });
            boardsContainer.appendChild(boardElement);
        });
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
});
