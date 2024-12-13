
// 追蹤是否為收縮狀態
let isMinimized = false;
//切換聊天室縮放
function toggleChat() {
    const chatWidget = document.getElementById('chat-widget_ft');
    const chatBody = document.getElementById('chat-body_ft');
    const returnIcon = document.getElementById('return-icon_ft');

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
        // 如果處於聊天窗口，顯示返回符號
        if (document.getElementById('chat-window').style.display === 'flex') {
            returnIcon.style.display = 'block';
        }
        isMinimized = false;
    }
}

//點擊聊天對象後，變成聊天室窗，變更標題樣式、增加返回符號
function openChat(user) {
    const headerTitle = document.getElementById('header-title_ft');
    const userList = document.getElementById('user-list_ft');
    const chatWindow = document.getElementById('chat-window_ft');
    const returnIcon = document.getElementById('return-icon_ft');

    headerTitle.innerText = `${user}`; // 更新標題
    returnIcon.style.display = 'block'; // 顯示返回符號
    userList.style.display = 'none';
    chatWindow.style.display = 'flex';
}

//回到列表頁的方法(返回上頁後，斷離websocket連線)
function goBackToUserList(event) {
    // 阻止觸發 header 的收縮/展開功能
    event.stopPropagation();

    const headerTitle = document.getElementById('header-title_ft');
    const userList = document.getElementById('user-list_ft');
    const chatWindow = document.getElementById('chat-window_ft');
    const returnIcon = document.getElementById('return-icon_ft');

    headerTitle.innerText = ''; // 清空標題文字
    returnIcon.style.display = 'none'; // 隱藏返回符號
    chatWindow.style.display = 'none';
    userList.style.display = 'block';
}


		let list =document.getElementById("user-list_ft");
		
		//點擊聊聊，將該占卜師加到聊天室列表裡面
		list.addEventListener("click",function(e){
			if(e.target.classList.contains("mem")){
				openChat(e.target.innerText);
				connect(e.target.innerText);
			}
		});
		
		
			
		let stompClient =null;	
					
				//點擊聊天室窗時，與該占卜師建立連線，並訂閱該占卜師，可接收來自占卜師的訊息
				function connect(memname){
					//已連線的情況
					if (stompClient !== null) {
					     console.log('已經連線，直接訂閱');	
						 		 	 
//						 stompClient.subscribe('/user/'+memname+'/queue/messages', function (message) {
//		 	   				 const json = JSON.parse(message.body);
//		 				     showMessage(json);       
//		 	   		     });				 				 
						 			   
		 		       stompClient.subscribe('/user/queue/messages', function (message) {
							console.log('收到歷史訊息:', message.body);
						       const historyMsg = JSON.parse(message.body);
						       if (Array.isArray(historyMsg)) {
						           historyMsg.forEach(msg => showMessage(msg));
						       }else{
								showMessage(historyMsg);
							   }
		 		       });
		 
						 getHistory();
						 
					     return;
					 }			
					
					// 使用 SockJS 創建 WebSocket 連接，訂閱該占卜師
						const socket = new SockJS('/ws/fortuneTeller'); // WebSocket 端點
						stompClient = Stomp.over(socket); // 初始化 Stomp 客戶端
							
					   // 連接到 WebSocket 伺服器
					   stompClient.connect({}, function (frame) {
					       console.log('Connected: ' + frame);
						   
					       // 訂閱目的地
//						   stompClient.subscribe('/user/queue/messages', function (message) {
//				   				 const chatMessageObj = JSON.parse(message.body);
//							     showMessage(chatMessageObj);       
//				   		     });
							 
						   
					       stompClient.subscribe('/user/queue/messages', function (message) {
								console.log('這裡是訂閱自己:', message.body);
							       const historyMsg = JSON.parse(message.body);
							       if (Array.isArray(historyMsg)) {
							           historyMsg.forEach(msg => showMessage(msg));
							       }else{
										showMessage(historyMsg);
								   }
					       });
						   
						   getHistory(); //訂閱之後才發送歷史紀錄的請求
					   });			   
					   			   
				}
				
				//<顯示訊息的方法>
				function showMessage(message){
					   if (message) {
						
							let chatDisplay =document.querySelector("#chat-display_ft");
					       // 建立新的訊息元素
					       const messageElement = document.createElement('div');
					       messageElement.textContent = `${message.message}`;
					       messageElement.style.marginBottom = '10px';
					       messageElement.style.padding = '10px';
					       messageElement.style.backgroundColor = '#e6f7ff'; /* 訊息背景顏色 */
					       messageElement.style.borderRadius = '5px';
						   messageElement.style.width='50%';
						   
						   const self =document.getElementById("text_name_ft").innerText;
						   if (message.sender === self) {
					              // 如果是自己，訊息在右側
					              messageElement.style.backgroundColor = '#e6f7ff'; // 蓝色背景
					              messageElement.style.alignSelf = 'flex-end';
					          } else {
					              // 如果是會員，訊息在左側
					              messageElement.style.backgroundColor = '#fff2e6'; // 橙色背景
					              messageElement.style.alignSelf = 'flex-start';
					          }
							  			   
					       chatDisplay.appendChild(messageElement);
						   chatDisplay.scrollTop = chatDisplay.scrollHeight; 
						   

				}
						
			 }
			 
			 
			 // <按下按鈕送出訊息>
			 function sendMessage() {
			     const input = document.getElementById('message-input_ft');
			     const chatDisplay = document.getElementById('chat-display_ft');
			     const message = input.value.trim();
				 
				const memname =document.getElementById('header-title_ft').innerText;//取得會員名字
				 const self =document.getElementById("text_name_ft").innerText;
				 
				 if(message && stompClient){
					
					//用物件裝傳送的訊息
					const sendobj ={
						"type" : "",
						"sender" : self,
						"receiver" :memname,
						"message": message,
						"sendTime":""
					}
					
					stompClient.send('/app/chat',{},JSON.stringify(sendobj)); //轉成JSON傳到後端的API
					
				 }
				 
				 
			     if (message) {
			         const messageElement = document.createElement('div');
			         messageElement.textContent = `${message}`;
			         messageElement.style.marginBottom = '10px';
			         messageElement.style.padding = '10px';
			         messageElement.style.backgroundColor = '#e6f7ff'; /* 訊息背景顏色 */
			         messageElement.style.borderRadius = '5px';
					 messageElement.style.width='50%';
					 messageElement.style.alignSelf='flex-end';

			         chatDisplay.appendChild(messageElement);

			         // 清空輸入框
			         input.value = '';

			         // 滾動到最新訊息
			         chatDisplay.scrollTop = chatDisplay.scrollHeight;
			     }
			 }
			 
			 
			 //<按下Enter送出訊息>
			 let text_area =document.getElementById("message-input_ft");
			 text_area.addEventListener("keydown",function(e){
				//按下Enter也可觸發發送訊息的方法
			 	if(e.key =="Enter"){
			 		sendMessage();
			 	}
			 });
			 
			 
			 //取得歷史紀錄的方法
			 function getHistory(){

			const memname =document.getElementById('header-title_ft').innerText;
			 const self =document.getElementById("text_name_ft").innerText;	
				//用物件裝傳送的訊息
				const sendobj ={
					"type" : "history",
					"sender" : self,
					"receiver" :memname,
					"message": "",
					"sendTime":""
				}
				
				stompClient.send('/app/history',{},JSON.stringify(sendobj));
						
			 }
			 	   