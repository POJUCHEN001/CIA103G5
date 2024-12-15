// 追蹤是否為收縮狀態
let isMinimized = true;//預設為縮起

//切換聊天室縮放
function toggleChat() {
	const chatWidget = document.getElementById('chat-widget');
	const chatBody = document.getElementById('chat-body');
	const returnIcon = document.getElementById('return-icon');

	if (!isMinimized) {
		// 收縮到最底部
		chatWidget.style.height = '45px';
		chatBody.style.display = 'none';
		returnIcon.style.display = 'none'; // 隱藏返回符號
		isMinimized = true;
	} else {
		// 展開到原始大小
		chatWidget.style.height = '400px';
		chatBody.style.display = 'flex';
		const headerTitle = document.getElementById('header-title');
		const userList = document.getElementById('user-list');
		const chatWindow = document.getElementById('chat-window');
		const returnIcon = document.getElementById('return-icon');

		//fetch API 顯示會員所有的聊天對象
		fetch(`/chat/list`)
			.then(res => {
				// 檢查是否為 200 OK
				if (res.ok) {
					return res.json(); // 將響應轉為 JSON
				} else {
					// 處理非 200 狀態碼的響應
					return res.text().then(error => {
						throw new Error(error); // 拋出錯誤供 catch 處理
					});
				}
			})
			.then(data => {
				headerTitle.innerText = ''; // 清空標題文字
				returnIcon.style.display = 'none'; // 隱藏返回符號
				chatWindow.style.display = 'none';
				userList.style.display = 'block';


				userList.innerHTML = "";//每次都先清空列表
				//把抓到的資料迭代放出來，創建li標籤(name)、p標籤(id)
				data.forEach(ft => {
					let li = document.createElement("li");					
					li.textContent = ft[0]; 
					li.style.listStyle = "none";
					
					let hiddenLi =document.createElement("p");	
					hiddenLi.style.display="none";
					hiddenLi.innerText=ft[1];
				
					userList.appendChild(li);
					li.appendChild(hiddenLi);
				
				});

			})
			.catch(error => {
				// 處理錯誤情況
				console.error("Error fetching fortuneteller info:", error);
				alert(error.message); // 彈出錯誤提示
			});

		// 如果處於聊天窗口，顯示返回符號
		if (document.getElementById('chat-window').style.display === 'flex') {
			returnIcon.style.display = 'block';
		}
		isMinimized = false;
	}
}

//點擊聊天對象，開啟個人聊天視窗->變更標題為該對象、增加返回符號
function openChat(user) {
	const headerTitle = document.getElementById('header-title');
	const userList = document.getElementById('user-list');
	const chatWindow = document.getElementById('chat-window');
	const returnIcon = document.getElementById('return-icon');

	headerTitle.innerText = `${user}`; // 更新標題
	returnIcon.style.display = 'block'; // 顯示返回符號
	userList.style.display = 'none';
	chatWindow.style.display = 'flex';
}

//回到列表頁的方法(返回上頁後，斷離websocket連線)
//觸發時，fetch個人的好友清單
function goBackToUserList(event) {
	// 阻止觸發 header 的收縮/展開功能
	event.stopPropagation();

	const headerTitle = document.getElementById('header-title');
	const userList = document.getElementById('user-list');
	const chatWindow = document.getElementById('chat-window');
	const returnIcon = document.getElementById('return-icon');

	//fetch API 顯示會員的聊天對象
	fetch(`/chat/list`)
		.then(res => {
			// 檢查是否為 200 OK
			if (res.ok) {
				return res.json(); // 將響應轉為 JSON
			} else {
				// 處理非 200 狀態碼的響應
				return res.text().then(error => {
					throw new Error(error); // 拋出錯誤供 catch 處理
				});
			}
		})
		.then(data => {
			headerTitle.innerText = ''; // 清空標題文字
			returnIcon.style.display = 'none'; // 隱藏返回符號
			chatWindow.style.display = 'none';
			userList.style.display = 'block';

			userList.innerHTML = "";
			//把抓到的資料迭代放出來
			data.forEach(ft => {
						let li = document.createElement("li");					
						li.textContent = ft[0]; 
						li.style.listStyle = "none";
						
						let hiddenLi =document.createElement("p");	
						hiddenLi.style.display="none";
						hiddenLi.innerText=ft[1];
					
						userList.appendChild(li);
						li.appendChild(hiddenLi);
					
					});

		})
		.catch(error => {
			// 處理錯誤情況
			console.error("Error fetching fortuneteller info:", error);
			alert(error.message); // 彈出錯誤提示
		});

}


let list = document.getElementById("user-list");

//點擊聊聊，將該占卜師加到聊天室列表裡面(同時占卜師也加入該會員->後端service)
let chat_btn = document.getElementById("chat_with_ft");
chat_btn.addEventListener("click", function() {

	//直接展開聊天室，顯示視窗
	let ft = document.querySelector("input.ft");
	let ftId = ft.value.trim();
//	console.log(ftId);

	//fetch ft的名字
	if (ftId) {
		// 使用 fetch 調用後端 API
		fetch(`/chat/ftinfo?ftId=${ftId}`)
			.then(res => {
				// 檢查是否為 200 OK
				if (res.ok) {
					return res.json(); // 將響應轉為 JSON
				} else {
					// 處理非 200 狀態碼的響應
					return res.text().then(error => {
						throw new Error(error); // 拋出錯誤供 catch 處理
					});
				}
			})
			.then(data => {
				// 成功獲取占卜師資訊
//				console.log("Fortuneteller Info:", data);

				openChat(data.nickname);//直接傳入占卜師的暱稱，進入聊天視窗
				connect(data.nickname); //建立連線
//				console.log(data.nickname);

			})
			.catch(error => {
				// 處理錯誤情況
				console.error("Error fetching fortuneteller info:", error);
				alert(error.message); 
			});
	} else {
		// 處理 ftId 為空的情況
		alert("Please enter a valid fortuneteller ID.");
	}


	//fetch API 將ftId 加入個人好友
	fetch("/chat/add/friend", {
		method: "POST", // 指定為 POST 方法
		headers: {
			"Content-Type": "application/x-www-form-urlencoded" // 根據後端解析參數方式選擇類型
		},
		body: `ftId=${ftId}` // 以 URL 編碼格式發送參數
	})
		.then(response => {
			if (response.ok) {
				return response.text(); // 獲取響應文字
			} else {
				return response.text().then(error => {
					throw new Error(error);
				});
			}
		})
		.then(data => {
			console.log("成功添加新朋友：");
			alert(data); // 提示用戶成功
		})
		.catch(error => {
			console.error("添加新朋友失敗：", error);
			alert(error.message); // 提示用戶錯誤
		});


});

//點擊聊天對象，跳轉到聊天視窗
$("#user-list").on("click", "li", function() {
	const liName = $(this).contents().filter(function() {
	     return this.nodeType === 3; // 篩選純文字節點
	 }).text().trim();
	
//	console.log(liName);
	openChat(liName); //開啟聊天視窗
	
//	console.log("p標籤的值"+$(this).find("p").text());
//	connect($(this).find("p").text());//點擊該聊天對象時，建立websocket連線
	connect(liName); //websocket連線

});


let stompClient = null;

// 儲存當前的聊天對象
let currentChatTarget = null;

// 連接到指定聊天對象
function connect(ftname) {
    // 如果正在與另一個對象聊天，斷開當前連線
    if (stompClient !== null) {
        console.log(`正在切換聊天對象，斷開與 ${currentChatTarget} 的連線`);
        disconnect();
    }

    // 更新目前聊天對象，存入該對象的名字
    currentChatTarget = ftname;

    // 建立新的 WebSocket 連線
    const socket = new SockJS("/ws/member");
    stompClient = Stomp.over(socket);
    stompClient.heartbeat.outgoing = 10000; // 每 10 秒發送一次心跳
    stompClient.heartbeat.incoming = 10000; // 每 10 秒檢查服務端心跳

    stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        subscribeToChannel(ftname);
        getHistory(); // 訂閱完成後請求歷史訊息
    });
}

// 訂閱指定的聊天頻道
function subscribeToChannel(ftname) {
    const selfnickname = getSelfNickName();

    // 訂閱歷史訊息、自己的訊息的通道->通道:/cutstom/會員(發送者):占卜師(接收者)
    stompClient.subscribe(`/custom/${selfnickname}:${ftname}`, function(message) {
//        console.log(`收到歷史訊息:`, message.body);
        const historyMsg = JSON.parse(message.body);
        if (Array.isArray(historyMsg)) {
            historyMsg.forEach(msg => showMessage(msg));
        } else {
            showMessage(historyMsg);
        }
    });

    // 訂閱對方即時訊息的通道->通道:/custom/發送者(對方):接收者(自己)
    stompClient.subscribe(`/custom/${ftname}:${selfnickname}`, function(message) {
//        console.log(`收到即時訊息:`, message.body);
        const instantMsg = JSON.parse(message.body);
        showMessage(instantMsg);
        
    });

}

// 斷開 WebSocket 連線
function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect(() => {
            console.log("WebSocket 已斷開");
        });
    }
    stompClient = null;
    currentChatTarget = null;
}


//<顯示訊息的方法>
//function showMessage(message) {
//	if (message) {
//
//		let chatDisplay = document.querySelector("#chat-display");
//		// 建立新的訊息元素
//		const messageElement = document.createElement('div');
//		messageElement.textContent = `${message.message}`;
//		messageElement.style.marginBottom = '10px';
//		messageElement.style.padding = '10px';
//		messageElement.style.backgroundColor = '#e6f7ff'; /* 訊息背景顏色 */
//		messageElement.style.borderRadius = '5px';
//		messageElement.style.width='50%';
//
//
//		let selfnickname = getSelfNickName();
//
//		if (message.sender === selfnickname) {
//			// 如果是自己，訊息在右側
//			messageElement.style.backgroundColor = '#e6f7ff'; // 蓝色背景
//			messageElement.style.alignSelf = 'flex-end';
//		} else {
//			// 如果是占卜師，訊息在左側
//			messageElement.style.backgroundColor = '#fff2e6'; // 橙色背景
//			messageElement.style.alignSelf = 'flex-start';
//		}
//
//		chatDisplay.appendChild(messageElement);
//		chatDisplay.scrollTop = chatDisplay.scrollHeight;
//		
//		// 清空輸入框
//		const input = document.getElementById('message-input');
//	    input.value = '';
//	   
//
//	}
//
//}

function showMessage(message) {
    if (message) {
        let chatDisplay = document.querySelector("#chat-display");

        // 建立新的訊息元素
        const messageElement = document.createElement('div');
        messageElement.style.display = 'flex';
        messageElement.style.flexDirection = 'column';
        messageElement.style.marginBottom = '10px';
        messageElement.style.padding = '10px';
        messageElement.style.backgroundColor = '#e6f7ff'; // 訊息背景顏色
        messageElement.style.borderRadius = '5px';
        messageElement.style.width = '50%';
        messageElement.style.position = 'relative';

        let selfnickname = getSelfNickName();

        if (message.sender === selfnickname) {
            // 如果是自己，訊息在右側
            messageElement.style.backgroundColor = '#e6f7ff'; // 蓝色背景
            messageElement.style.alignSelf = 'flex-end';
        } else {
            // 如果是占卜師，訊息在左側
            messageElement.style.backgroundColor = '#fff2e6'; // 橙色背景
            messageElement.style.alignSelf = 'flex-start';
        }

        // 設定訊息內容
        const textContent = document.createElement('span');
        textContent.textContent = `${message.message}`;
        textContent.style.wordWrap = 'break-word';

        // 設定時間標籤
        const timeElement = document.createElement('span');
        timeElement.textContent = message.sendTime; // 從 JSON 中取得時間
        timeElement.style.fontSize = '0.8em';
        timeElement.style.color = '#888'; // 灰色文字
        timeElement.style.alignSelf = 'flex-end';
        timeElement.style.marginTop = '5px';

        // 將訊息與時間加入訊息框
        messageElement.appendChild(textContent);
        messageElement.appendChild(timeElement);

        // 將訊息框加入顯示區
        chatDisplay.appendChild(messageElement);
        chatDisplay.scrollTop = chatDisplay.scrollHeight;

        // 清空輸入框
        const input = document.getElementById('message-input');
        input.value = '';
    }
}



// <按下按鈕送出訊息>
function sendMessage() {
	const input = document.getElementById('message-input');
	const message = input.value.trim();

	let selfnickname = getSelfNickName(); //取得會員名字
	const receiverName = document.getElementById('header-title').innerText; //取得占卜師名字


	if (message && stompClient) {

		//用物件裝傳送的訊息
		const sendobj = {
			"type": "",
			"sender": selfnickname,
			"receiver": receiverName,
			"message": message,
			"sendTime": ""
		}

		stompClient.send('/app/chat', {}, JSON.stringify(sendobj)); //轉成JSON傳到後端的API

	}

}


//<按下Enter送出訊息>
let text_area = document.getElementById("message-input");
text_area.addEventListener("keydown", function(e) {
	//按下Enter也可觸發發送訊息的方法
	if (e.key == "Enter") {
		sendMessage();
	}
});


//取得歷史紀錄的方法
function getHistory() {

	const ftname = document.getElementById('header-title').innerText;
	let selfnickname = getSelfNickName();

	//用物件裝傳送的訊息
	const sendobj = {
		"type": "history",
		"sender": selfnickname,
		"receiver": ftname,
		"message": "",
		"sendTime": ""
	}

	//發出取得共同歷史紀錄的請求，到指定路徑/app/history，傳入物件，取得共同歷史訊息(發送者或接收者是誰都沒差，同一份資料)
	stompClient.send('/app/history', {}, JSON.stringify(sendobj));

}


//取得自己的暱稱
function getSelfNickName() {
	const memberInfo = sessionStorage.getItem('memberInfo');
	let nickname = null;

	if (memberInfo) {
		const memberObject = JSON.parse(memberInfo);
		nickname = memberObject.nickname;
	}

	return nickname;

}


//取得自己的memId
function getSelfId() {
	const memberInfo = sessionStorage.getItem('memberInfo');
	let id = null;

	if (memberInfo) {
		const memberObject = JSON.parse(memberInfo);
		id = memberObject.memberId;
	}

	return id;

}





