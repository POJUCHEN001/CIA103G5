document.addEventListener("DOMContentLoaded", () => {
    const ftForm = document.getElementById("ftForm");

    ftForm.addEventListener("submit", async (event) => {
        event.preventDefault();

        const formData = new FormData(ftForm);

        // 從 sessionStorage 獲取 memberId
        const memberId = sessionStorage.getItem("memberId");
        if (!memberId) {
            alert("會員編號缺失，請重新註冊");
            window.location.href = "/register"; // 返回到第一步
            return;
        }

        // 將 memberId 添加到 FormData 中
        formData.append("memberId", memberId);

        try {
            const response = await fetch("/user/ft-register", {
                method: "POST",
                body: formData,
            });

            if (response.ok) {
                alert("註冊成功！請等待審核");
                window.location.href = "/ftcenter"; // 跳轉到成功頁面
            } else {
                const error = await response.json();
                alert("提交失敗：" + (error.message || "未知錯誤"));
            }
        } catch (error) {
            console.error("提交過程中發生錯誤：", error);
            alert("提交過程中發生錯誤，請稍後再試！");
        }
    });
});