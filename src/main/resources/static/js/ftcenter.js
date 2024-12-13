/**
 * 
 */

// 載入占卜師基本資料
//async function loadMemberProfile() {
//    // 使用 fetch API 從後端獲取占卜師資料
//    await fetch('/ftAPI/info', { method: "GET" , credentials: 'include'})
//        .then(function (response) {
//            // 檢查是否成功獲取資料，否則拋出錯誤
//            if (!response.ok) {
//				if(response.status === 401){
//					alert("未登入，請前往登入");				
//				} else if (response.status === 404){
//					alert("沒有占卜師身分");
//				}else{
//	                throw new Error('無法獲取占卜師資料');					
//				}
//			} return;
//            return response.json(); // 解析 JSON 資料
//        })
//        .then(function (data) {
//            // 更新 HTML 中的占卜師資料
//            document.getElementById('ftId').textContent = data.ftId;
//            document.getElementById('nickname').textContent = data.nickname;
//            document.getElementById('companyName').textContent = data.companyName || '未提供';
//            document.getElementById('businessNo').textContent = data.businessNo || '未提供';
//
//            // 假設 ftRank 包含 rankName，顯示等級名稱
//            document.getElementById('ftRank').textContent = data.ftRank.rankName;
//
//            // 格式化日期並更新到頁面
//            document.getElementById('registeredTime').textContent = data.registeredTime
//                ? new Date(data.registeredTime).toLocaleString()
//                : '已提交申請';
//            document.getElementById('approvedTime').textContent = data.approvedTime
//                ? new Date(data.approvedTime).toLocaleString()
//                : '尚未審核';
//
//            // 顯示價格與簡介
//            document.getElementById('price').textContent = data.price || '尚未設置價格';
//            document.getElementById('intro').textContent = data.intro || '暫無簡介';
//
//            // 照片處理邏輯
//            const profilePhoto = document.getElementById('photo');
//            const businessPhoto = document.getElementById('businessPhoto');
//
//            // 處理個人照片
//            if (data.photo) {
//                profilePhoto.src = 'data:image/' + data.photoFormat + ';base64,' + data.photo;
//            } else {
//                profilePhoto.src = '/img/default-photo.png'; // 預設照片
//            }
//
//            // 處理營業執照照片
//            if (data.businessPhoto) {
//                businessPhoto.src = 'data:image/' + data.photoFormat + ';base64,' + data.businessPhoto;
//            } else {
//                businessPhoto.src= '/img/businessphoto01.jpg'; // 預設照片
//            }
//        })
//        .catch(function (error) {
//            // 錯誤處理，顯示錯誤訊息
//            console.error(error);
////            alert('請重新登入！');
////            window.location.href = "/login";
//        });
//}

async function loadMemberProfile() {
    try {
        const response = await fetch('/ftAPI/info', { 
            method: "GET", 
            credentials: 'include' 
        });

        // 處理非 2xx 狀態碼
        if (!response.ok) {
            if (response.status === 401) {
                alert("未登入，請前往登入");
                window.location.href = "/login"; // 重導到登入頁面
            } else if (response.status === 404) {
                alert("未找到占卜師資料，請確認您的權限");
            } else {
                throw new Error('無法獲取占卜師資料');
            }
            return; // 提前結束函數
        }

        // 解析返回的 JSON 資料
        const data = await response.json();

        // 更新 HTML 中的占卜師資料
        updateFtProfile(data);

    } catch (error) {
        console.error("獲取占卜師資料時發生錯誤：", error);
        alert("沒有訪問權限或未登入");
    }
}

// 抽取更新資料邏輯到獨立函數
function updateFtProfile(data) {
    document.getElementById('ftId').textContent = data.ftId || 'N/A';
    document.getElementById('nickname').textContent = data.nickname || '未提供';
    document.getElementById('companyName').textContent = data.companyName || '未提供';
    document.getElementById('businessNo').textContent = data.businessNo || '未提供';
    document.getElementById('ftRank').textContent = data.ftRank?.rankName || 'N/A';
    document.getElementById('registeredTime').textContent = data.registeredTime 
        ? new Date(data.registeredTime).toLocaleString() 
        : '已提交申請';
    document.getElementById('approvedTime').textContent = data.approvedTime 
        ? new Date(data.approvedTime).toLocaleString() 
        : '尚未審核';
    document.getElementById('price').textContent = data.price || '尚未設置價格';
    document.getElementById('intro').textContent = data.intro || '暫無簡介';

    const profilePhoto = document.getElementById('photo');
    const businessPhoto = document.getElementById('businessPhoto');

    profilePhoto.src = data.photo 
        ? `data:image/jpeg;base64,${data.photo}` 
        : '/img/default-photo.png';
    businessPhoto.src = data.businessPhoto 
        ? `data:image/jpeg;base64,${data.businessPhoto}` 
        : '/img/businessphoto01.jpg';
}



// 當 DOM 完全加載後執行
// document.addEventListener('DOMContentLoaded', function () {
//     loadMemberProfile();
// });
// 頁面載入時自動執行
window.addEventListener("DOMContentLoaded", loadMemberProfile);

// 修改資料的樣式 (點擊修改占卜師資料)
document.getElementById("edit-info-btn").addEventListener("click", function () {
    // 切換到編輯模式
    document.getElementById("info-view").style.display = "none";
    document.getElementById("edit-section").style.display = "inline-block";

    // 將原有資料填入輸入框
    document.getElementById("edit-nickname").value = document.getElementById("nickname").textContent;
    // document.getElementById("edit-companyName").value = document.getElementById("companyName").textContent;
    // document.getElementById("edit-businessNo").value = document.getElementById("businessNo").textContent;
    // document.getElementById("edit-bankAccount").value = document.getElementById("bankAccount").textContent;
    if(document.getElementById("price").textContent != "尚未設置價格"){
        document.getElementById("edit-price").value = document.getElementById("price").textContent;
    }
    if(document.getElementById("intro").textContent != "暫無簡介"){
        document.getElementById("edit-intro").value = document.getElementById("intro").textContent;
    }

});

document.getElementById("cancel-edit-btn").addEventListener("click", function () {
    // 取消修改，恢復到查看模式
    document.getElementById("info-view").style.display = "inline-block";
    document.getElementById("edit-section").style.display = "none";
});


// 點擊送出 修改服務價格與簡介
document.getElementById("save-edit-btn").addEventListener("click", function () {

    // 保存修改，將輸入框的值更新到顯示區域

    document.getElementById("nickname").textContent = document.getElementById("edit-nickname").value;
    document.getElementById("intro").textContent = document.getElementById("edit-intro").value;
    document.getElementById("price").textContent = document.getElementById("edit-price").value;

    // Fetch FtController 的 /updateinfo
    const nickname = document.getElementById("edit-nickname").value;
    const intro = document.getElementById("edit-intro").value;
    const price = document.getElementById("edit-price").value;

    // 驗證價格是否為有效數字
    if (!price || isNaN(price) || Number(price) < 0) {
        alert("請輸入有效的服務價格（正整數）");
        return;
    }

    const data = {
        nickname: nickname,
        intro: intro,
        price: parseInt(price, 10)
    }
    fetch('/ftAPI/updateinfo',{
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
    .then(response => {
        if(!response.ok){
            throw new Error('請輸入正確的資料格式')
        }
        return response.text();
    })
    .then(message => {
        alert(message);


    // 更新顯示區域的內容
    document.getElementById("nickname").textContent = nickname;
    document.getElementById("intro").textContent = intro;
    document.getElementById("price").textContent = price;

    // 切回查看模式
    document.getElementById("info-view").style.display = "inline-block";
    document.getElementById("edit-section").style.display = "none";
    })
        .catch(error => {
            alert(`錯誤：${error.message}`)
        })
});

// 更換占卜師形象照
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
            const response = await fetch(`/ftAPI/updatephoto`, {
                method: 'PATCH',
                credentials: 'include',  // 確保附帶 Cookie
                body: formData,
            });

            if (response.ok) {
                const result = await response.json();
                // alert(result.message);
                // 照片更新後重新載入占卜師資料
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

// 會員登入頭像



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