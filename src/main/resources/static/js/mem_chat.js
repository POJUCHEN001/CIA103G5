
//import SockJS from 'sockjs-client';
//import { Client, Stomp } from '@stomp/stompjs';


// 追蹤是否為收縮狀態
let isMinimized = false;
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

		//fetch API 顯示會員的聊天對象
		// 使用 fetch 調用後端 API
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
					li.innerHTML = ft;
					li.style.listStyle = "none";

					userList.appendChild(li);
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

//點擊聊天對象後，變成聊天室窗，變更標題樣式、增加返回符號
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
//觸發時，要fetch個人的好友清單
function goBackToUserList(event) {
	// 阻止觸發 header 的收縮/展開功能
	event.stopPropagation();

	const headerTitle = document.getElementById('header-title');
	const userList = document.getElementById('user-list');
	const chatWindow = document.getElementById('chat-window');
	const returnIcon = document.getElementById('return-icon');

	//fetch API 顯示會員的聊天對象

	// 使用 fetch 調用後端 API
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
				li.innerHTML = ft;
				li.style.listStyle = "none";

				userList.appendChild(li);
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
	console.log(ftId);


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
				console.log("Fortuneteller Info:", data);

				openChat(data.nickname);
				connect();
				console.log(data.nickname);


			})
			.catch(error => {
				// 處理錯誤情況
				console.error("Error fetching fortuneteller info:", error);
				alert(error.message); // 彈出錯誤提示
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

//點擊聊天對象，跳轉到聊天視窗、建立
$("#user-list").on("click", "li", function() {
	let ft_name = $(this).html();
	console.log(ft_name);
	openChat(ft_name);
	connect(ft_name);//點擊該聊天對象時，建立websocket連線

});


let stompClient = null;

//點擊聊天室窗時，與該占卜師建立連線，訂閱自己，可接收占卜師傳送給自己的訊息
function connect(sessionId) {

	//已連線的情況
	if (stompClient !== null) {
		console.log('已經連線，直接訂閱');

		let selfnickname = getSelfNickName();
		console.log('訂閱的路徑: /user/' + selfnickname + '/queue/messages');
		stompClient.subscribe('/user/${sessionId}/queue/messages', function(message) {
			console.log('收到歷史訊息:', message.body);
			const historyMsg = JSON.parse(message.body);
			if (Array.isArray(historyMsg)) {
				historyMsg.forEach(msg => showMessage(msg));
			} else {
				showMessage(historyMsg);
			}
		});

		getHistory();

		return;
	}
	
	var MyPoint = "/chat";
	var host = window.location.host;
	var path = window.location.pathname;
	var webCtx = path.substring(0, path.indexOf('/', 1));
	var endPointURL = "ws://" + window.location.host + webCtx + MyPoint;
	
	
	// 使用 SockJS 創建 WebSocket 連接，訂閱該占卜師
	const socket = new SockJS("/ws/member"); // WebSocket 端點
	stompClient = Stomp.over(socket); // 初始化 Stomp 客戶端
	stompClient.heartbeat.outgoing = 10000; // 每 10 秒發送一次心跳
	stompClient.heartbeat.incoming = 10000; // 每 10 秒檢查服務端心跳

	// 連接到 WebSocket 伺服器
	stompClient.connect({}, function(frame) {
		console.log('Connected: ' + frame);

		let selfnickname = getSelfNickName();
		console.log('訂閱的路徑: /user/' + selfnickname + '/queue/messages');
		
		stompClient.subscribe('/user/queue/messages', function(message) {
			console.log('message:----------------------' + message);
			try{
			
				console.log('這裡是訂閱自己:', message.body);
				const historyMsg = JSON.parse(message.body);
				if (Array.isArray(historyMsg)) {
					historyMsg.forEach(msg => showMessage(msg));
				} else {
					showMessage(historyMsg);
				}				
				
			}catch{
				console.error("錯誤訊息");
			}
		});

		getHistory(); //訂閱之後才發送歷史紀錄的請求
	});

}

//<顯示訊息的方法>
function showMessage(message) {
	if (message) {

		let chatDisplay = document.querySelector("#chat-display");
		// 建立新的訊息元素
		const messageElement = document.createElement('div');
		messageElement.textContent = `${message.message}`;
		messageElement.style.marginBottom = '10px';
		messageElement.style.padding = '10px';
		messageElement.style.backgroundColor = '#e6f7ff'; /* 訊息背景顏色 */
		messageElement.style.borderRadius = '5px';


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

		chatDisplay.appendChild(messageElement);
		chatDisplay.scrollTop = chatDisplay.scrollHeight;

	}

}


// <按下按鈕送出訊息>
function sendMessage() {
	const input = document.getElementById('message-input');
	const chatDisplay = document.getElementById('chat-display');
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


	if (message) {
		// 建立新的訊息元素，會員端顯示訊息
		const messageElement = document.createElement('div');
		messageElement.textContent = `${message}`;
		messageElement.style.marginBottom = '10px';
		messageElement.style.padding = '10px';
		messageElement.style.backgroundColor = '#e6f7ff'; /* 訊息背景顏色 */
		messageElement.style.borderRadius = '5px';
		messageElement.style.width = '50%';
		messageElement.style.alignSelf = 'flex-end';

		chatDisplay.appendChild(messageElement);

		// 清空輸入框
		input.value = '';

		// 滾動到最新訊息
		chatDisplay.scrollTop = chatDisplay.scrollHeight;
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

	console.log("history的地方印出來:" + ftname);
	console.log("history的地方印出來:" + selfnickname);
	//用物件裝傳送的訊息
	const sendobj = {
		"type": "history",
		"sender": selfnickname,
		"receiver": ftname,
		"message": "",
		"sendTime": ""
	}

	stompClient.send('/app/history', {}, JSON.stringify(sendobj));

}

function getSelfNickName() {
	const memberInfo = sessionStorage.getItem('memberInfo');
	let nickname = null;

	if (memberInfo) {
		const memberObject = JSON.parse(memberInfo);
		nickname = memberObject.nickname;
	}

	return nickname;

}


//	 window.onload = connect;