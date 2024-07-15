document.getElementById('signupForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const accountId = document.getElementById('accountId').value;
    const password = document.getElementById('password').value;
    const userName = document.getElementById('userName').value;
    const adminToken = document.getElementById('adminToken').value;
    const admin = document.getElementById('admin').checked;
    const errorElement = document.getElementById('error');
    errorElement.textContent = '';

    try {
        const response = await fetch('http://localhost:8080/api/user/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ accountId, password, userName, adminToken, admin }),
        });

        if (!response.ok) {
            errorElement.textContent = '회원가입 중 오류가 발생했습니다.';
            return;
        }

        const result = await response.json();

        if (result.statusCode === 200) {  // 성공 코드 확인
            alert('회원가입이 완료되었습니다.');
            window.location.href = '/';
        } else {
            errorElement.textContent = result.message || '회원가입 실패. 입력 내용을 확인하세요.';
        }
    } catch (error) {
        errorElement.textContent = '회원가입 중 오류가 발생했습니다.';
    }
});
