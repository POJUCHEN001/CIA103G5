$(document).ready(function () {
    // 發送文章的按鈕點擊事件
    $("#post-submit").click(function (event) {
        event.preventDefault(); // 阻止表單的預設提交行為
        console.log("發送文章請求觸發");

        // 收集表單資料
        const category = $("#post-category").val();
        const title = $("#post-title").val().trim();
        const author = $("#post-author").val().trim();
        const content = $("#post-content").html().trim(); // 獲取包含圖片的 HTML 內容

        // 檢查必填項
        if (!category) {
            alert("請選擇發文版區！");
            return;
        }
        if (!title) {
            alert("請填寫標題！");
            return;
        }
        if (!author) {
            alert("請填寫作者！");
            return;
        }
        if (!content) {
            alert("請填寫文章內容！");
            return;
        }

        // 組裝發送的資料
        const postData = {
            title: title,
            authorName: author,
            content: content,
            postCategory: { categoryName: category } // 將分類名稱傳遞到後端
        };

        console.log("發送的資料：", postData); // 檢查發送的資料內容

        // 使用 fetch 發送文章的 POST 請求
        fetch('/api/posts', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(postData)
        })
        .then(response => {
            if (!response.ok) {
                // 如果伺服器回傳非成功狀態，抓取錯誤訊息
                return response.json().then(errorData => {
                    throw new Error(`伺服器錯誤: ${errorData.message}`);
                });
            }
            return response.json(); // 回傳文章資料
        })
        .then(post => {
            if (!post.postNo) {
                throw new Error("無效的文章編號，無法進行圖片上傳");
            }
            console.log("文章發表成功，伺服器回應:", post);
            alert("文章發表成功！");
            // 上傳圖片
            return uploadImages(post.postNo);
        })
        .then(() => {
            // 跳轉到 /forum 頁面
            window.location.href = "/forum";
        })
        .catch(error => {
            console.error("發表文章時發生錯誤：", error);
            alert(`儲存文章時發生錯誤: ${error.message}`);
        });
    });

    // 插入圖片按鈕點擊事件
    $("#insert-image").click(function () {
        $("#image-input").click(); // 觸發隱藏的文件選擇器
    });

    // 文件選擇變化事件，用於選擇圖片並顯示在內容中
    $("#image-input").change(function () {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function (e) {
                // 將圖片插入到 #post-content 中，以便顯示在文章中
                const imgHtml = `<img src="${e.target.result}" alt="插入的圖片" style="max-width: 100%;">`;
                $("#post-content").append(imgHtml);
            };
            reader.readAsDataURL(file); // 讀取文件並在完成後觸發 onload 事件
        }
    });

    /**
     * 上傳圖片至指定的文章
     * @param {number} postNo - 剛發表的文章編號
     * @returns {Promise} - 返回 Promise，表示圖片上傳完成
     */
    function uploadImages(postNo) {
        return new Promise((resolve, reject) => {
            const files = $("#image-input")[0].files;
            if (files.length === 0) {
                console.log("沒有圖片要上傳。");
                resolve();
                return;
            }

            // 構造 FormData 以便上傳圖片
            const formData = new FormData();
            for (let i = 0; i < files.length; i++) {
                formData.append("pic", files[i]); // 確保這裡使用 "pic" 與後端一致
            }

            // 使用 fetch 發送圖片的 POST 請求
            fetch(`/api/posts/${postNo}/uploadPic`, {
                method: 'POST',
                body: formData
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error("圖片上傳失敗");
                }
                return response.text(); // 取得回應訊息
            })
            .then(data => {
                console.log("圖片上傳成功，伺服器回應:", data);
                alert("圖片上傳成功！");
                // 清空文件選擇器，為下一次上傳做準備
                $("#image-input").val('');
                resolve();
            })
            .catch(error => {
                console.error("圖片上傳時發生錯誤：", error);
                alert("圖片上傳失敗！");
                reject(error);
            });
        });
    }
});


function toggleFavorite(button) {
    const postNo = button.getAttribute("data-post");

    fetch('/api/favorite', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `postNo=${postNo}`
    })
    .then(response => response.text())
    .then(data => {
        alert(data); // 顯示伺服器回應
    })
    .catch(error => console.error('錯誤:', error));
}

function toggleLike(button) {
    const postNo = button.getAttribute("data-post");

    fetch(`/api/like/${postNo}`, {
        method: 'POST',
    })
    .then(response => location.reload())
    .catch(error => console.error('錯誤:', error));
}

function toggleDislike(button) {
    const postNo = button.getAttribute("data-post");

    fetch(`/api/dislike/${postNo}`, {
        method: 'POST',
    })
	.then(response => location.reload())
    .catch(error => console.error('錯誤:', error));
}

function editArticle(button) {
	const postNo = button.getAttribute("data-post");
	button.disabled = true;
	
	// 隱藏文章內容
	const postArea = document.getElementById("post-content");
	const content = document.getElementById("post-textarea");
	postArea.style.display = "none";

	// 顯示編輯區域，並將原始文章內容填入文本框
	const editArea = document.getElementById("post-edit");
	const editButton = document.getElementById("save-edited-btn");
	document.getElementById("edit-content").value = content.textContent;
	editArea.style.display = "block";
}

function saveEditedContent(button) {
	const postNo = button.getAttribute("data-post");
	
	// 收集表單資料
	const category = document.getElementById("post-category-content");
	const title = document.getElementById("post-title-content");
	const author = document.getElementById("post-author-content");
	const content = document.getElementById("edit-content"); // 獲取包含圖片的 HTML 內容
	
	// 組裝發送的資料
	const postData = {
		title: title.textContent.trim(),
		authorName: author.textContent.trim(),
		content: content.value.trim(),
		postCategory: { categoryName: category.textContent } // 將分類名稱傳遞到後端
	};

	console.log("發送的資料：", postData); // 檢查發送的資料內容

	// 使用 fetch 發送文章的 PUT 請求
	fetch(`/api/posts/${postNo}`, {
	    method: 'PUT',
	    headers: {
	        'Content-Type': 'application/json'
	    },
	    body: JSON.stringify(postData)
	})	
	.then(response => location.reload())
	.catch(error => console.error('錯誤:', error));
}

function deleteArticle(button) {
	const postNo = button.getAttribute("data-post");
	const category = document.getElementById("post-category-content");
	
	// 使用 fetch 發送文章的 DELETE 請求
	fetch(`/api/posts/${postNo}`, {
	    method: 'DELETE',
	})	
	.then(response => window.location.href = "/category/" + category.textContent)
	.catch(error => console.error('錯誤:', error));
}

function saveCommentContent(button) {
	const postNo = button.getAttribute("data-post");
	
	// 收集表單資料
	const content = document.getElementById("new-comment-content");
	
	// 組裝發送的資料
	const commentData = {
		commentText: content.value.trim(),
	};

	console.log("發送的留言：", commentData); // 檢查發送的資料內容

	// 使用 fetch 發送文章的 PUT 請求
	fetch(`/api/comment/${postNo}`, {
	    method: 'POST',
	    headers: {
	        'Content-Type': 'application/json'
	    },
	    body: JSON.stringify(commentData)
	})	
	.then(response => location.reload())
	.catch(error => console.error('錯誤:', error));
}

function deleteComment(button) {
	const commentNo = button.getAttribute("data-post");
	
	// 使用 fetch 發送留言的 DELETE 請求
	fetch(`/api/comment/${commentNo}`, {
	    method: 'DELETE',
	})	
	.then(response => location.reload())
	.catch(error => console.error('錯誤:', error));
}

function openReportModal(button) {
	document.getElementById("post-report").style.display = "block";
}

function submitReport(button) {
	const postNo = button.getAttribute("data-post");
	const reason = document.getElementById("reportReason").value;
	const content = document.getElementById("reportContent").value.trim();
	
	if (!reason || content === "") {
	    alert("請選擇檢舉原因並填寫檢舉內容！");
	    return;
	}

	const reportData = {
	    postNo: parseInt(postNo, 10),// 以十進位解析文章編號 postNo，確保為數字
	    reason: parseInt(reason, 10),// 以十進位解析檢舉原因 reason，確保為數字
	    content: content,// 檢舉內容，直接傳遞字串
	};
	
	fetch(`/api/report`, {
	    method: "POST",
	    headers: {
	        "Content-Type": "application/json",
	    },
	    body: JSON.stringify(reportData),
	})
    .then((response) => {
        if (response.ok) {
            alert("檢舉成功！");
        } else {
            alert("檢舉失敗，請稍後再試！");
        }
		document.getElementById("post-report").style.display = "none";
    })
    .catch((error) => {
        console.error("檢舉提交時出現錯誤:", error);
        alert("檢舉失敗，請稍後再試！");
		document.getElementById("post-report").style.display = "none";
    });
}

function cancelReport(button) {
	document.getElementById("post-report").style.display = "none";
	document.getElementById("reportReason").value = "0";
	document.getElementById("reportContent").value = "";
}

//從前端接收表單並將資料送到後端再存入資料庫，可能會用到 AJAX，但根據我的專案架構和所使用的技術堆疊，是利用了 Spring MVC 的表單綁定機制，並由 Spring Data JPA 負責存取資料庫。

//主要的流程與技術
//表單綁定與控制器處理：

//當使用者在前端提交表單時，資料會送到指定的控制器（如 PostController）進行處理。
//在控制器中，可以透過 @RequestMapping、@PostMapping 等註解來處理該請求，並用類別的物件來綁定表單中的資料。例如， Post 類別會用來接收表單的內容。
//Spring MVC 的表單綁定機制：

//Spring MVC 可以將前端表單中的資料直接映射到後端的物件（例如 Post 類別的屬性），這樣可以讓資料傳遞和綁定更為簡單，不需要在前端處理過多資料轉換。
//AJAX（若使用）：

//若使用了 AJAX，那麼在前端就會使用 JavaScript 或 jQuery 等方式發送非同步請求，將表單資料傳送到後端的 API。
//AJAX 的優勢在於可以非同步地更新資料，而不需要重新載入頁面。
//在專案中，若是透過 JavaScript 發送的請求，那麼這部分應該是用 AJAX 技術來達成。
//Spring Data JPA 負責儲存：

//在控制器接收到資料後，會透過 Spring Data JPA 將表單資料存入資料庫。這一部分可能會呼叫 PostRepository，透過它來執行儲存操作。
//JPA 會將物件映射到資料庫的對應表格中，並負責 SQL 指令的生成與執行。

//我的專案中應該是主要使用了 Spring MVC 的表單綁定機制，再搭配 Spring Data JPA 來完成資料的持久化。至於是否用到 AJAX，則取決於是否有在前端以 JavaScript 發送非同步請求。

//我的專案中是 AJAX 和 Spring Data JPA 的混合使用。這樣的組合運行方式如下：

//AJAX 和 Spring Data JPA 的合作方式
//前端（AJAX）發送請求：

//使用者在前端提交表單後，AJAX 會攔截這個操作並使用 JavaScript（如 jQuery 的 $.ajax 或 fetch）來發送非同步請求，通常是 HTTP POST 請求，將表單資料（如文章標題、內容等）以 JSON 格式發送給後端的 API。
//後端（Spring MVC 控制器）接收請求：

//在 Spring Boot 中，控制器（如 PostController）負責處理這個 AJAX 請求。控制器中的方法通常會用 @PostMapping 來標記，指定接受的 URL 路徑。
//Spring MVC 自動將 AJAX 傳送的 JSON 資料解析為 Java 類別（例如 Post 類別的物件），這樣就可以將前端傳來的資料轉換為後端可操作的物件。
//Spring Data JPA 儲存資料：

//控制器在取得 Post 類別的資料後，會呼叫 Spring Data JPA 的 Repository（如 PostRepository）將資料保存到資料庫。
//Spring Data JPA 會根據提供的物件，生成適當的 SQL 指令並將其執行，以便將資料存入對應的資料表。
//AJAX 收到回應並更新前端：

//當資料成功儲存後，Spring 控制器會返回一個回應（例如 JSON 格式的狀態訊息或新增的文章資料），並在 AJAX 請求的 .then 或 .done 回調中接收。
//前端使用這個回應進行畫面的更新，例如顯示成功訊息或將新增的文章直接插入列表中，讓使用者即時看到變動。
//為何這麼做
//前端即時回饋：AJAX 允許非同步更新，無需刷新頁面，使用者可以即時看到結果。
//後端資料處理自動化：Spring Data JPA 能自動生成並執行 SQL 指令，大幅減少手動撰寫 SQL 的繁瑣工作，也確保了資料操作的一致性和安全性。
//效能與體驗最佳化：AJAX 避免了頁面重載，提升了使用者體驗；而 Spring Data JPA 則提高了開發效能，使得後端儲存邏輯簡化且可維護。
//運行原理
//AJAX 非同步運行：AJAX 會在背景中發送 HTTP 請求，而不影響頁面其他部分的運行。
//Spring Data JPA 持久化運行：Spring Data JPA 利用 JPA 實體（如 Post 類別）和 Repository 來抽象化資料庫操作，利用物件與資料庫的映射，將物件自動轉換為資料表操作。
//這樣的搭配使得系統能即時接收並儲存資料，同時保持介面不刷新，非常適合需要高互動性的應用。
