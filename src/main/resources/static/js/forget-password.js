/**
 * 
 */

document.getElementById('forgotPasswordForm').addEventListener('submit', async function(event) {
    event.preventDefault(); // 防止表單默認提交

    const email = document.getElementById('forgotEmail').value;

    try {
        const response = await fetch('/user/forgot-password', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email }),
        });

        if (response.ok) {
            const result = await response.json();
            alert(result.message || '連結已成功發送至信箱，請前往查看');
            // 關閉 Modal
            const modalElement = document.getElementById('forgotPasswordModal');
            const modalInstance = bootstrap.Modal.getInstance(modalElement);
            modalInstance.hide();
        } else {
            const error = await response.json();
            alert(error.message || '發送失敗，請確認Email是否正確');
        }
    } catch (error) {
        console.error('Error:', error);
//        alert('An unexpected error occurred. Please try again later.');
    }
});
