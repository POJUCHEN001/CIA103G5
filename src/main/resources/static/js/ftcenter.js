/**
 * 
 */
// 載入占卜師基本資料
// 當 DOM 完全加載後執行
document.addEventListener('DOMContentLoaded', function () {
    // 假設占卜師 ID 固定為 1，實際可以通過其他方式動態獲取
    var ftId = 1;

    // 使用 fetch API 從後端獲取占卜師資料
    fetch('/ft/info/' + ftId)
        .then(function (response) {
            // 檢查是否成功獲取資料，否則拋出錯誤
            if (!response.ok) {
                throw new Error('無法獲取占卜師資料');
            }
            return response.json(); // 解析 JSON 資料
        })
        .then(function (data) {
            // 更新 HTML 中的占卜師資料
            document.getElementById('ftId').textContent = ftId;
            document.getElementById('nickname').textContent = data.nickname || '未提供';
            document.getElementById('companyName').textContent = data.companyName || '未提供';
            document.getElementById('businessNo').textContent = data.businessNo || '未提供';

            // 假設 ftRank 包含 rankName，顯示等級名稱
            document.getElementById('ftRank').textContent = data.ftRank.rankName || '未提供';

            // 格式化日期並更新到頁面
            document.getElementById('registeredTime').textContent = data.registeredTime
                ? new Date(data.registeredTime).toLocaleString()
                : '尚未註冊';
            document.getElementById('approvedTime').textContent = data.approvedTime
                ? new Date(data.approvedTime).toLocaleString()
                : '尚未審核';

            // 顯示價格與簡介
            document.getElementById('price').textContent = data.price || '未設定';
            document.getElementById('intro').textContent = data.intro || '暫無簡介';

            // 照片處理邏輯
            const profilePhoto = document.getElementById('photo');
            const businessPhoto = document.getElementById('businessPhoto');

            // 處理個人照片
            if (data.photo) {
                profilePhoto.src = 'data:image/' + data.photoFormat + ';base64,' + data.photo;
            } else {
                profilePhoto.src = './img/default-profile.png'; // 預設照片
            }

            // 處理營業執照照片
            if (data.businessPhoto) {
                businessPhoto.src = 'data:image/' + data.photoFormat + ';base64,' + data.businessPhoto;
            } else {
                businessPhoto.src = './img/default-profile.png'; // 預設照片
            }
        })
        .catch(function (error) {
            // 錯誤處理，顯示錯誤訊息
            console.error(error);
            alert('無法獲取占卜師資料，請稍後重試！');
        });
});



// 修改資料的樣式
document.getElementById("edit-info-btn").addEventListener("click", function () {
    // 切換到編輯模式
    document.getElementById("info-view").style.display = "none";
    document.getElementById("edit-section").style.display = "inline-block";

    // 將原有資料填入輸入框
    document.getElementById("edit-nickname").value = document.getElementById("nickname").textContent;
    document.getElementById("edit-companyName").value = document.getElementById("companyName").textContent;
    document.getElementById("edit-businessNo").value = document.getElementById("businessNo").textContent;
    document.getElementById("edit-bankAccount").value = document.getElementById("bankAccount").textContent;
    document.getElementById("edit-intro").value = document.getElementById("intro").textContent;
    document.getElementById("edit-price").value = document.getElementById("price").textContent;
});

document.getElementById("cancel-edit-btn").addEventListener("click", function () {
    // 取消修改，恢復到查看模式
    document.getElementById("info-view").style.display = "inline-block";
    document.getElementById("edit-section").style.display = "none";
});

document.getElementById("save-edit-btn").addEventListener("click", function () {
    // 保存修改，將輸入框的值更新到顯示區域
    document.getElementById("nickname").textContent = document.getElementById("edit-nickname").value;
    document.getElementById("intro").textContent = document.getElementById("edit-intro").value;
    document.getElementById("price").textContent = document.getElementById("edit-price").value;

    // 切回查看模式
    document.getElementById("info-view").style.display = "inline-block";
    document.getElementById("edit-section").style.display = "none";
});
