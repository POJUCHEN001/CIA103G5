/****************************
/ *                         *
/ *                         *
/          變更密碼         
/ *                         *
/ *                         *
/ ***************************/



document.getElementById('passwordForm').addEventListener('submit', async function(e) {
	e.preventDefault(); // 阻止表單默認提交行為

	const currentPassword = document.getElementById('currentPassword').value;
	const newPassword = document.getElementById('newPassword').value;
	const confirmPassword = document.getElementById('confirmPassword').value;

	// 清空所有錯誤訊息
	clearErrors();

	let hasError = false;

	// 驗證新密碼格式
	const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/;
	if (!passwordRegex.test(newPassword)) {
		showError('newPasswordError', '密碼需至少8位，包含大小寫字母和數字');
		hasError = true;
	}

	// 驗證新密碼與確認密碼是否一致
	if (newPassword !== confirmPassword) {
		showError('confirmPasswordError', '新密碼與確認密碼不一致');
		hasError = true;
	}

	// 如果有錯誤，不進行提交
	if (hasError) return;

	try {
		const response = await fetch('/membersAPI/changepassword', {
			method: 'POST',
			credentials: 'include',  // 確保附帶 Cookie
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({
				currentPassword: currentPassword,
				newPassword: newPassword
			})
		});

		// 檢查回應是否成功
		if (!response.ok) {
			const result = await response.json();
			alert(result.message || "密碼修改失敗");
			return;
		}

		// 解析並處理成功訊息
		const result = await response.json();
		alert(result.message || "密碼修改成功");
		window.location.href = "/login";
	} catch (error) {
		// 捕捉網路異常
		alert("發生錯誤，請稍後再試");
		console.error("Error:", error);
	}
});

// 工具函數：清空所有錯誤訊息
function clearErrors() {
	document.querySelectorAll('.error_message').forEach(el => (el.textContent = ''));
}

// 工具函數：顯示錯誤訊息
function showError(elementId, message) {
	const errorElement = document.getElementById(elementId);
	if (errorElement) {
		errorElement.textContent = message;
	}
}
