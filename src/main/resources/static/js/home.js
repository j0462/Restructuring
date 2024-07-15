document.addEventListener('DOMContentLoaded', () => {
    const boardId = new URLSearchParams(window.location.search).get('boardId');
    const boardTitle = document.getElementById('board-title');
    const columnsContainer = document.getElementById('columns-container');
    const addColumnBtn = document.getElementById('add-column-btn');
    let draggedColumn = null;

    fetchBoardDetails(boardId);
    fetchColumns(boardId);

    function fetchBoardDetails(boardId) {
        fetch(`/api/board/${boardId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
        })
            .then(response => response.json())
            .then(data => {
                if (data.statusCode === 200) {
                    displayBoardDetails(data.data);
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('보드 설명 가져오기 실패:', error);
                alert('보드 설명 가져오기 실패');
            });
    }

    function fetchColumns(boardId) {
        fetch(`/api/boards/${boardId}/columns`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
        })
            .then(response => response.json())
            .then(data => {
                if (data.statusCode === 200) {
                    displayColumns(data.data);
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('컬럼 가져오기 실패:', error);
                alert('컬럼 가져오기 실패');
            });
    }

    function displayBoardDetails(board) {
        boardTitle.innerText = `${board.boardName} - ${board.boardExplain}`;
    }

    function displayColumns(columns) {
        columnsContainer.innerHTML = '';
        columns.sort((a, b) => a.columnOrder - b.columnOrder); // Sort columns by order
        columns.forEach(column => {
            const columnElement = document.createElement('div');
            columnElement.classList.add('column');
            columnElement.setAttribute('draggable', true);
            columnElement.dataset.columnId = column.columnId;
            columnElement.dataset.columnOrder = column.columnOrder;
            columnElement.innerHTML = `
                <div class="column-title">${column.columnName}</div>
                <div class="cards" id="cards-${column.columnName}"></div>
                <button class="add-card-btn" onclick="addCard(${column.columnId}, '${column.columnName}')">+ 카드 추가</button>
            `;
            columnsContainer.appendChild(columnElement);
            fetchCards(column.columnName);

            // Add drag and drop event listeners
            columnElement.addEventListener('dragstart', handleDragStart);
            columnElement.addEventListener('dragover', handleDragOver);
            columnElement.addEventListener('drop', handleDrop);
            columnElement.addEventListener('dragend', handleDragEnd);
        });
    }

    function fetchCards(columnName) {
        fetch(`/api/card/status/${columnName}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            },
        })
            .then(response => response.json())
            .then(data => {
                if (data.statusCode === 200) {
                    displayCards(columnName, data.data);
                } else {
                    alert(data.message);
                }
            })
            .catch(error => {
                console.error('카드 가져오기 실패:', error);
                alert('카드 가져오기 실패');
            });
    }

    function displayCards(columnName, cards) {
        const cardsContainer = document.getElementById(`cards-${columnName}`);
        cardsContainer.innerHTML = '';
        cards.forEach(card => {
            const cardElement = document.createElement('div');
            cardElement.classList.add('card');
            cardElement.innerHTML = `
                <div class="card-title">${card.title}</div>
                <div class="card-content">${card.content}</div>
                <div class="card-date">${card.date}</div>
            `;
            cardsContainer.appendChild(cardElement);
        });
    }

    window.addCard = function(columnId, columnName) {
        const cardTitle = prompt('카드 제목:');
        const cardContent = prompt('카드 내용:');
        const cardDate = prompt('카드 마감일 (YYYY-MM-DD):');
        if (cardTitle && cardContent && cardDate) {
            fetch('/api/card', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify({ title: cardTitle, content: cardContent, date: cardDate, columnStatus: columnName }),
            })
                .then(response => response.json())
                .then(data => {
                    if (data.statusCode === 200) {
                        fetchCards(columnName);
                    } else {
                        alert(data.message);
                    }
                })
                .catch(error => {
                    console.error('카드 생성 실패:', error);
                    alert('카드 생성 실패');
                });
        }
    };

    addColumnBtn.addEventListener('click', () => {
        const columnName = prompt('컬럼 이름:');
        if (columnName) {
            fetch(`/api/boards/${boardId}/columns`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify({ columnName: columnName, boardId: boardId }),
            })
                .then(response => response.json())
                .then(data => {
                    if (data.statusCode === 200) {
                        fetchColumns(boardId);
                    } else {
                        alert(data.message);
                    }
                })
                .catch(error => {
                    console.error('컬럼 생성 실패:', error);
                    alert('컬럼 생성 실패');
                });
        }
    });

    function handleDragStart(event) {
        draggedColumn = event.currentTarget;
        event.dataTransfer.effectAllowed = 'move';
        event.dataTransfer.setData('text/html', draggedColumn.outerHTML);
        draggedColumn.classList.add('dragging');
    }

    function handleDragOver(event) {
        event.preventDefault();
        event.dataTransfer.dropEffect = 'move';
        const targetColumn = event.currentTarget;
        if (targetColumn !== draggedColumn) {
            const allColumns = Array.from(columnsContainer.querySelectorAll('.column'));
            const draggedIndex = allColumns.indexOf(draggedColumn);
            const targetIndex = allColumns.indexOf(targetColumn);
            if (draggedIndex > targetIndex) {
                columnsContainer.insertBefore(draggedColumn, targetColumn);
            } else {
                columnsContainer.insertBefore(draggedColumn, targetColumn.nextSibling);
            }
        }
    }

    function handleDrop(event) {
        event.stopPropagation();
        if (draggedColumn !== event.currentTarget) {
            draggedColumn.outerHTML = event.currentTarget.outerHTML;
            event.currentTarget.outerHTML = event.dataTransfer.getData('text/html');
            const allColumns = Array.from(columnsContainer.querySelectorAll('.column'));
            allColumns.forEach((column, index) => {
                const columnId = column.dataset.columnId;
                updateColumnOrder(columnId, index);
            });
        }
        draggedColumn.classList.remove('dragging');
        draggedColumn = null;
    }

    function handleDragEnd() {
        const allColumns = Array.from(columnsContainer.querySelectorAll('.column'));
        allColumns.forEach((column, index) => {
            const columnId = column.dataset.columnId;
            updateColumnOrder(columnId, index);
        });
        draggedColumn.classList.remove('dragging');
        draggedColumn = null;
    }

    function updateColumnOrder(columnId, newOrder) {
        const currentOrder = parseInt(document.querySelector(`[data-column-id="${columnId}"]`).dataset.columnOrder, 10);
        if (currentOrder !== newOrder) {
            fetch(`/api/boards/${columnId}/${newOrder}`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
            })
                .then(response => response.json())
                .then(data => {
                    if (data.statusCode !== 200) {
                        alert(data.message);
                    } else {
                        // 업데이트 성공 후 데이터셋의 columnOrder를 업데이트
                        document.querySelector(`[data-column-id="${columnId}"]`).dataset.columnOrder = newOrder;
                    }
                })
                .catch(error => {
                    console.error('컬럼 순서 변경 실패:', error);
                    alert('컬럼 순서 변경 실패');
                });
        }
    }
});
