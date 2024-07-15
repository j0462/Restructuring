document.getElementById('loginForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const accountId = document.getElementById('accountId').value;
    const password = document.getElementById('password').value;
    const errorElement = document.getElementById('error');
    errorElement.textContent = '';

    try {
        const response = await fetch('http://localhost:8080/api/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ accountId, password }),
        });

        if (!response.ok) {
            const errorData = await response.json();
            errorElement.textContent = errorData.message || '로그인 중 오류가 발생했습니다.';
            return;
        }

        const result = await response.json();

        if (result.statusCode === 200) {  // 성공 코드 확인
            alert('로그인에 성공했습니다.');
            window.location.href = '/index';
        } else {
            errorElement.textContent = result.message || '로그인 실패. 아이디와 비밀번호를 확인하세요.';
        }
    } catch (error) {
        errorElement.textContent = '로그인 중 오류가 발생했습니다.';
    }
});
