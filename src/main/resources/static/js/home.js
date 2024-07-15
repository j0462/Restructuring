document.addEventListener('DOMContentLoaded', () => {
    const boardId = new URLSearchParams(window.location.search).get('boardId');
    const boardTitle = document.getElementById('board-title');
    const columnsContainer = document.getElementById('columns-container');
    const addColumnBtn = document.getElementById('add-column-btn');
    let draggedColumn = null;

    const modal = document.getElementById("edit-modal");
    const span = document.getElementsByClassName("close")[0];
    const modalTitle = document.getElementById("modal-title");
    const editTitle = document.getElementById("edit-title");
    const editContent = document.getElementById("edit-content");
    const editDate = document.getElementById("edit-date");
    const saveEditBtn = document.getElementById("save-edit-btn");

    let currentEditType = '';
    let currentEditId = null;

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
                <div class="column-title">
                    ${column.columnName}
                    <button class="edit-btn" onclick="editColumn(${column.columnId}, '${column.columnName}')">수정</button>
                    <button class="delete-btn" onclick="deleteColumn(${column.columnId})">삭제</button>
                </div>
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
                <div class="card-title">
                    ${card.title}
                    <button class="edit-btn" onclick="editCard(${card.id}, '${card.title}', '${card.content}', '${card.date}')">수정</button>
                    <button class="delete-btn" onclick="deleteCard(${card.id})">삭제</button>
                </div>
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

    window.editColumn = function(columnId, columnName) {
        currentEditType = 'column';
        currentEditId = columnId;
        modalTitle.innerText = '컬럼 수정';
        editTitle.value = columnName;
        editContent.style.display = 'none';
        editDate.style.display = 'none';
        modal.style.display = 'block';
    };

    window.deleteColumn = function(columnId) {
        if (confirm('정말 이 컬럼을 삭제하시겠습니까?')) {
            fetch(`/api/boards/${boardId}/${columnId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
            })
                .then(response => response.json())
                .then(data => {
                    if (data.statusCode === 200) {
                        location.reload(); // 페이지 새로고침
                    } else {
                        alert(data.message);
                    }
                })
                .catch(error => {
                    console.error('컬럼 삭제 실패:', error);
                    alert('컬럼 삭제 실패');
                });
        }
    };

    window.editCard = function(cardId, cardTitle, cardContent, cardDate) {
        currentEditType = 'card';
        currentEditId = cardId;
        modalTitle.innerText = '카드 수정';
        editTitle.value = cardTitle;
        editContent.value = cardContent;
        editDate.value = cardDate;
        editContent.style.display = 'block';
        editDate.style.display = 'block';
        modal.style.display = 'block';
    };

    window.deleteCard = function(cardId) {
        if (confirm('정말 이 카드를 삭제하시겠습니까?')) {
            fetch(`/api/card/${cardId}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
            })
                .then(response => response.json())
                .then(data => {
                    if (data.statusCode === 200) {
                        location.reload(); // 페이지 새로고침
                    } else {
                        alert(data.message);
                    }
                })
                .catch(error => {
                    console.error('카드 삭제 실패:', error);
                    alert('카드 삭제 실패');
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

    span.onclick = function() {
        modal.style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

    saveEditBtn.addEventListener('click', () => {
        if (currentEditType === 'column') {
            const columnName = editTitle.value;
            fetch(`/api/board/${currentEditId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify({ columnName: columnName }),
            })
                .then(response => response.json())
                .then(data => {
                    if (data.statusCode === 200) {
                        fetchColumns(boardId);
                        modal.style.display = "none";
                    } else {
                        alert(data.message);
                    }
                })
                .catch(error => {
                    console.error('컬럼 수정 실패:', error);
                    alert('컬럼 수정 실패');
                });
        } else if (currentEditType === 'card') {
            const cardTitle = editTitle.value;
            const cardContent = editContent.value;
            const cardDate = editDate.value;
            fetch(`/api/card/${currentEditId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify({ title: cardTitle, content: cardContent, date: cardDate }),
            })
                .then(response => response.json())
                .then(data => {
                    if (data.statusCode === 200) {
                        location.reload();
                    } else {
                        alert(data.message);
                    }
                })
                .catch(error => {
                    console.error('카드 수정 실패:', error);
                    alert('카드 수정 실패');
                });
        }
    });
});
