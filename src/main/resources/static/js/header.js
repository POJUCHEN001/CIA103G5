/**
 * 
 */

	// 取得HTML上的元素
	const register_el = document.getElementById("register_el");
	const login_el = document.getElementById("login_el");
	const dropdown_ftcenter_el = document.getElementById("dropdown-ftcenter");
	// 取得 sessionStorage 中的 isLogin 值
	const isLogin = sessionStorage.getItem('isLogin');
	const isFortuneTeller = sessionStorage.getItem('ftId');
	
	
	// 檢查登入狀態，隱藏不需要的元素
	if (isLogin === 'true') {
		register_el.style.display = 'none';
		login_el.style.display = 'none';
	}
	
	// 檢查是否為占卜師身分，再顯示占卜師中心
	if (isFortuneTeller === '0'){
		dropdown_ftcenter_el.style.display = 'none';
	}
		
		
	// 載入會員頭像
	document.addEventListener('DOMContentLoaded', function () {
        fetch('/membersAPI/info-photo')
            .then(response => response.ok ? response.json() : null)
            .then(data => {
                if (data && data.photo) {
                    const userAvatar = document.getElementById('userAvatar');
                    userAvatar.src = `data:image/png;base64,${data.photo}`;
                }
            })
            .catch(() => {
                // 不處理錯誤，默認保持訪客頭像
            });
    });
	
	
	
	// 登出按鈕
	document.querySelectorAll('.logout-link').forEach(logoutLink => {
	logoutLink.addEventListener('click', async function(event) {
		event.preventDefault(); // 阻止默認行為，如跳轉鏈接

// 		try {
			const response = await fetch('/membersAPI/logout', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				}
			});

			if (response.ok) {
				const result = await response.json();
				// alert(result.message || "登出成功");

				// 清除 sessionStorage 中的資訊
				sessionStorage.removeItem("loggedInAccount");
				sessionStorage.removeItem("memberInfo");
				sessionStorage.removeItem("isLogin");
				sessionStorage.removeItem("ftId");
				sessionStorage.removeItem("memberId");
				// 清空 sessionStorage 的所有資料
				// sessionStorage.clear();

				// 使用後端返回的 redirectURL 進行跳轉
				window.location.href = result.redirectURL;
			} else {
				const result = await response.json();
				// alert(result.message || "登出失敗，請稍後再試");
			}
// 		} catch (error) {
// 			console.error("登出發生錯誤：", error);
// 		}
		});
	});
	
	