// 追蹤是否為收縮狀態
let isMinimized = true;//預設為縮起

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
		
	   const headerTitle = document.getElementById('header-title_ft');
	   const userList = document.getElementById('user-list_ft');
	   const chatWindow = document.getElementById('chat-window_ft');
	   const returnIcon = document.getElementById('return-icon_ft');

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
				 	
					
					userList.innerHTML="";
					//把抓到的資料迭代放出來
					data.forEach(ft=>{
						let li =document.createElement("li");				
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
        if (document.getElementById('chat-window_ft').style.display === 'flex') {
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
					 	
						userList.innerHTML="";
						//把抓到的資料迭代放出來
						data.forEach(ft=>{
							let li =document.createElement("li");				
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


		let list =document.getElementById("user-list_ft");
		
		//點擊人名，跳轉到該聊天視窗、建立連線
		list.addEventListener("click",function(e){
			if (e.target.tagName === 'LI') {
		       // 取得 <li> 的名字
		       const liName = e.target.childNodes[0].nodeValue.trim();
		       console.log('點擊的名字:', liName);
				
			   openChat(liName);
			   connect(liName);
			   }					
			
		});
		
		let stompClient = null;
		let currentChatTarget = null; // 當前聊天對象

		function connect(name) {
		    // 如果已經有連線，先斷開
		    if (stompClient !== null) {
		        console.log('正在切換聊天對象，斷開當前連線');
		        disconnect();
		    }

		    // 更新當前聊天對象
		    currentChatTarget = name;

		    // 建立新的 WebSocket 連線
		    const socket = new SockJS('/ws/fortuneTeller');
		    stompClient = Stomp.over(socket);

		    stompClient.connect({}, function (frame) {
		        console.log('Connected: ' + frame);

		        let selfnickname = getSelfNickName();

		        // 訂閱歷史訊息、對方的即時訊息的通道->/custom/sender(會員):receiver(自己)
		        stompClient.subscribe('/custom/' + name + ':' + selfnickname, function (message) {
		            console.log('收到歷史或對方的即時訊息:', message.body);
		            const historyMsg = JSON.parse(message.body);
		            if (Array.isArray(historyMsg)) {
		                historyMsg.forEach(msg => showMessage(msg));
		            } else {
		                showMessage(historyMsg);
		            }
		        });

		        getHistory(); // 訂閱完成後請求歷史訊息

				//自己的即時訊息的通道->/custom/sender(自己):receiver(對方)
		        stompClient.subscribe('/custom/' + selfnickname + ':' + name, function (message) {
		            console.log('收到自己的即時消息:', message.body);
		            showMessage(JSON.parse(message.body));
		        });
		    });
		}

		// 斷開 WebSocket 連線
		function disconnect() {
		    if (stompClient !== null) {
		        stompClient.disconnect(() => {
		            console.log('WebSocket 已斷開');
		        });
		    }
		    stompClient = null;
		    currentChatTarget = null;
		}

				
				//<顯示訊息的方法>
//				function showMessage(message){
//					   if (message) {
//						
//						   let chatDisplay =document.querySelector("#chat-display_ft");
//					       // 建立新的訊息元素
//					       const messageElement = document.createElement('div');
//					       messageElement.textContent = `${message.message}`;
//					       messageElement.style.marginBottom = '10px';
//					       messageElement.style.padding = '10px';
//					       messageElement.style.backgroundColor = '#e6f7ff'; /* 訊息背景顏色 */
//					       messageElement.style.borderRadius = '5px';
//						   messageElement.style.width='50%';
//						   
//						   let selfnickname =getSelfNickName();
//						   if (message.sender === selfnickname) {
//					              // 如果是自己，訊息在右側
//					              messageElement.style.backgroundColor = '#e6f7ff'; // 蓝色背景
//					              messageElement.style.alignSelf = 'flex-end';
//					          } else {
//					              // 如果是會員，訊息在左側
//					              messageElement.style.backgroundColor = '#fff2e6'; // 橙色背景
//					              messageElement.style.alignSelf = 'flex-start';
//					          }
//							  			   
//					       chatDisplay.appendChild(messageElement);
//						   chatDisplay.scrollTop = chatDisplay.scrollHeight; 
//						    
//						    // 清空輸入框
//							const input = document.getElementById('message-input_ft');
//						    input.value = '';						   
//
//				}
//						
//			 }
			 
			function showMessage(message) {
			    if (message) {
			        let chatDisplay = document.querySelector("#chat-display_ft");
			
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
			        const input = document.getElementById('message-input_ft');
			        input.value = '';
			    }
			}
			

			 
			 // <按下按鈕送出訊息>
			 function sendMessage() {
			    const input = document.getElementById('message-input_ft');
			    const message = input.value.trim();
				 
				const memname =document.getElementById('header-title_ft').innerText;//取得會員名字
				let selfnickname =getSelfNickName();
				 
				 if(message && stompClient){
					
					//用物件裝傳送的訊息(sender:自己、receiver:會員)
					const sendobj ={
						"type" : "",
						"sender" : selfnickname,
						"receiver" :memname,
						"message": message,
						"sendTime":""
					}
					
					stompClient.send('/app/chat',{},JSON.stringify(sendobj)); //轉成JSON傳到後端的API
					
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
				let selfnickname =getSelfNickName();
					//用物件裝傳送的訊息(sender:會員、receiver:自己)-->跟會員發出一樣的請求，取得同一份
					const sendobj ={
						"type" : "history",
						"sender" :memname,
						"receiver" :selfnickname,
						"message": "",
						"sendTime":""
					}
					
					stompClient.send('/app/history',{},JSON.stringify(sendobj));
						
			 }
			 
			 
			 //取得自己的暱稱
			 function getSelfNickName(){
			 	const memberInfo = sessionStorage.getItem('memberInfo');
			 	let nickname=null;
			 				
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
			 	   