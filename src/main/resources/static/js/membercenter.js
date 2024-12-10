/****************************
/ *                         *
/ *                         *
/          會員中心         
/ *                         *
/ *                         *
/ ***************************/
document.addEventListener("DOMContentLoaded", function () {
  const emailStatusElement = document.getElementById("emailStatus");
  const resendButton = document.getElementById("resendButton");

  // 從後端獲取會員資訊
  async function fetchMemberInfo() {
    try {
      const response = await fetch("/membersAPI/info"); // API 返回會員資訊
     if (!response.ok) {
		 // throw new Error(`無法獲取會員資訊：${response.status} ${response.statusText}`);
		 // alert("您尚未登入，請先登入");
		 window.location.href = "/login";
		 return;
     }
      const member = await response.json();

      // 根據 emailState 更新顯示內容
      emailStatusElement.textContent = member.emailState === 1 ? "已驗證" : "未驗證";

      // 如果信箱未驗證，顯示重新寄送按鈕
      if (member.emailState === 0) {
        resendButton.style.display = "inline-block";
        // 綁定按鈕點擊事件
        resendButton.addEventListener("click", function () {
          resendVerificationCode(member.email);
        });
      }
    } catch (error) {
      console.error("錯誤：", error);
      emailStatusElement.textContent = "無法載入狀態";
    }
  }

  // 發送重新寄送驗證碼的請求
  async function resendVerificationCode(email) {
    try {
      const response = await fetch(`/membersAPI/send-verification-code?email=${encodeURIComponent(email)}`, {
        method: "POST",
      });
      if (!response.ok) {
        throw new Error("重新寄送驗證碼失敗");
      }
      alert("驗證碼已重新寄送到您的信箱，請查收！");
    } catch (error) {
      console.error("錯誤：", error);
      alert("無法重新寄送驗證碼，請稍後再試！");
    }
  }

  // 初始化頁面
  fetchMemberInfo();
});

// 載入會員資料
async function loadMemberProfile() {

	try{
		// 從 sessionStorage 獲取會員帳號
		const memberInfo = sessionStorage.getItem("memberInfo");

		if(!sessionStorage){
			window.location.href = "/login";
			alert("您尚未登入，請先登入");
			return;
		}
		if (!memberInfo) {
			window.location.href = "/login";
			alert("您尚未登入，請先登入");
			return;
		}
		const member = JSON.parse(memberInfo);

		// 向後端請求會員資料
		const response = await fetch(`/membersAPI/info`, { method: "GET" }, {credentials: 'include',});

		if (response.ok) {
			const member = await response.json();
			console.log(member);
			// 渲染會員資料到頁面
			document.getElementById("name").textContent = member.name;
			document.getElementById("account").textContent = member.account;
			document.getElementById("email").textContent = member.email;
			document.getElementById("emailStatus").textContent = member.emailState === 1 ? "已驗證" : "未驗證";
			document.getElementById("nickname").textContent = member.nickname;
			document.getElementById("gender").textContent = member.gender === 1 ? "男" : member.gender === 2 ? "女" : "不公開";
			document.getElementById("phone").textContent = member.phone;
			document.getElementById("points").textContent = member.points;
			document.getElementById("bankAccount").textContent = member.bankAccount || "尚未設置";
			document.getElementById("registeredTime").textContent = new Intl.DateTimeFormat('zh-TW', {
				year: 'numeric', month: '2-digit', day: '2-digit',
				hour: '2-digit', minute: '2-digit', second: '2-digit',
			}).format(new Date(member.registeredTime));
			
			

			function getStatusText(status) {
				switch (status) {
					case 0: return "啟用中";
					case 1: return "已停用";
					case 2: return "已註銷";
//					default: return "未知狀態";
				}
			}
			document.getElementById("status").textContent = getStatusText(member.status);

			// 顯示會員照片
			const profilePhoto = document.getElementById("photo");
			const profilePhotoIcon = document.getElementById("photo-icon");

			if (member.photo) {
				profilePhoto.src = `data:image/${member.photoFormat};base64,${member.photo}`;
				profilePhotoIcon.src = `data:image/${member.photoFormat};base64,${member.photo}`;
			} else {
				profilePhoto.src = "/img/default-photo.png"; // 預設照片
			}
		} else if (response.status === 404) {
			alert("會員資料不存在");
			window.location.href = "/login";
		} else {
			throw new Error("載入會員資料失敗");
		}
	} catch (error) {
		console.error("尚未登入", error);
		alert("尚未登入，將導向登入畫面！");
		window.location.href = "/login";
	}
}

// Fetch API: 上傳新照片
document.getElementById('update-photo-btn').addEventListener('click', async () => {
	const photoInput = document.createElement('input');
	photoInput.type = 'file';
	photoInput.accept = 'image/*';

	photoInput.addEventListener('change', async () => {
		const file = photoInput.files[0];
		if (file.size > 5 * 1024 * 1024) {
			alert('檔案過大，請選擇小於 5MB 的圖片。');
			return;
		}

		const formData = new FormData();
		formData.append('photo', file);


		try {
			// const account = sessionStorage.getItem("loggedInAccount");
			// if (!account) {
			// 	alert("無法獲取會員帳號，請重新登入！");
			// 	return;
			// }

			const response = await fetch(`/membersAPI/photo`, {
				method: 'PATCH',
				credentials: 'include',  // 確保附帶 Cookie
				body: formData,
			});

			if (response.ok) {
				const result = await response.json();
				// alert(result.message);
				// 照片更新後重新載入會員資料
				loadMemberProfile();
			} else {
				throw new Error('照片更新失敗');
			}
		} catch (error) {
			console.error(error);
			alert('更換照片失敗，請稍後再試。');
		}
	});
	photoInput.click();
});

// 頁面載入時自動執行
window.addEventListener("DOMContentLoaded", loadMemberProfile);


// 取得按鈕和表單容器
const editInfoBtn = document.getElementById("edit-info-btn");
const editModal = document.getElementById("edit-modal");
const closeModalBtn = document.getElementById("close-modal-btn");
const editForm = document.getElementById("edit-form");

// 點擊「修改基本資料」按鈕，顯示彈窗並填入資料
editInfoBtn.addEventListener('click', () => {
	// 取得當前會員資料
	//	const name = document.getElementById("name").textContent;
	//	const email = document.getElementById("email").textContent;
	//	const nickname = document.getElementById("nickname").textContent;
	//	const phone = document.getElementById("phone").textContent;
	//	const bankAccount = document.getElementById("bankAccount").textContent;
	//	const gender = document.getElementById("gender").textContent;

	// 填入現有資料到表單
	document.getElementById("edit-name").value = document.getElementById("name").textContent || "";
	document.getElementById("edit-nickname").value = document.getElementById("nickname").textContent || "";
	document.getElementById("edit-phone").value = document.getElementById("phone").textContent || "";
	if(!document.getElementById("bankAccount").textContent == "尚未設置"){
		document.getElementById("edit-bankAccount").value = document.getElementById("bankAccount").textContent || "";
	}

	// 性別選擇處理
	const gender = document.getElementById("gender").textContent;
	document.getElementById("edit-gender").value =
		gender === "男" ? "1" : gender === "女" ? "2" : "0";

	// 顯示&隱藏按鈕
	editModal.classList.remove("hidden");
	//           editInfoBtn.classList.add('hidden');

});


// 點擊關閉按鈕隱藏模態框
closeModalBtn.addEventListener("click", () => {
	editModal.classList.add("hidden");
	console.log("模態框已隱藏");
});

// 點擊模態框背景隱藏
editModal.addEventListener("click", (e) => {
	if (e.target === editModal) {
		editModal.classList.add("hidden");
		console.log("點擊背景，模態框已隱藏");
	}
});

// 表單提交邏輯 (修改會員基本資料)
editForm.addEventListener('submit', async (e) => {
	e.preventDefault();

	const updatedData = {
		name: document.getElementById("edit-name").value,
		nickname: document.getElementById("edit-nickname").value,
		phone: document.getElementById("edit-phone").value,
		bankAccount: document.getElementById("edit-bankAccount").value,
		gender: document.getElementById("edit-gender").value
	};
	
	// 驗證銀行帳號
	const bankAccountRegex = /^\d{3}-\d{7,16}$/; // 定義正則表示式
	if (updatedData.bankAccount && !bankAccountRegex.test(updatedData.bankAccount)) {
		alert("銀行帳號格式錯誤，請輸入正確格式（例如：123-1234567）");
		return; // 終止提交
	}

	try {
		const account = sessionStorage.getItem("loggedInAccount"); // 確保已正確存入帳號
		if (!account) {
			alert("無法取得會員帳號，請重新登入！");
			return;
		}

		const response = await fetch(`/membersAPI/updateinfo/${account}`, {
			method: "PATCH",
			credentials: 'include',  // 確保附帶 Cookie
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify(updatedData),
		});

		if (response.ok) {
			alert("會員資料已修改成功！");
			editModal.classList.add("hidden"); // 隱藏模態框
			loadMemberProfile(); // 重新載入會員資料
		} else {
			//			throw new Error("更新失敗");
			const errorData = await response.json();
			// alert(`修改失敗: ${errorData.message}`);
		}
	} catch (error) {
		console.error("更新會員資料失敗", error);
		// alert("修改失敗，請稍後再試！");
	}
});

// 登出功能
document.querySelectorAll('.logout-link').forEach(logoutLink => {
	logoutLink.addEventListener('click', async function(event) {
		event.preventDefault(); // 阻止默認行為，如跳轉鏈接

		try {
			const response = await fetch('/membersAPI/logout', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				}
			});

			if (response.ok) {
				const result = await response.json();
				// alert(result.message || "登出成功");

				// 清除 sessionStorage 中的帳號
				sessionStorage.removeItem("loggedInAccount");
				sessionStorage.removeItem("memberInfo");

				// 跳轉到登入頁面
				window.location.href = "/login";
			} else {
				const result = await response.json();
				// alert(result.message || "登出失敗，請稍後再試");
			}
		} catch (error) {
			console.error("登出發生錯誤：", error);
			alert("系統發生錯誤，請稍後再試");
		}
	});
});
