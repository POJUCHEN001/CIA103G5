//點擊評論按鈕時fetch API，在燈箱內顯示商品細項
$("div.order-container").on("click", "button.score-order", function () {
    const self = this; // 保存當前按鈕的上下文
    let orderNoString = $(self).closest("div.order-card").find("p.order-number").text();
    let orderNo = orderNoString.substring(8, orderNoString.length); // 擷取出訂單編號	
	
    console.log("訂單編號:", orderNo); // 調試：檢查訂單編號是否正確

    fetch(`/order/mem_order/modalInfo?orderNo=${orderNo}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then((res) => {
            if (!res.ok) {
                throw new Error(`HTTP error! Status: ${res.status}`);
            }
            return res.json();
        })
        .then((data) => {
            console.log("返回的數據:", data); // 調試：檢查後端返回的數據
            // 獲取全局燈箱的內容區域
//			console.log($(this))
//			console.log($(this).closest("div.order-card"))
//			console.log($(this).closest("div.order-card").find("div.modal") )
//			console.log($(this).closest("div.order-card").find("p.order-number").text())
			console.log($(this).closest("div.order-card").find("form#ratingForm").find("input").val());
			let value =$(this).closest("div.order-card").find("form#ratingForm").find("input").val();
            const modalContent = $("#ratingModal").find("form#ratingForm");
            modalContent.empty(); // 清空舊內容
			let input =document.createElement("input");
			input.value=value;
			input.style.display='none';
					
			modalContent.append(input);
            // 為每個商品生成評論區塊
            data.forEach((product) => {
                let score_block = `
                    <!-- 評分區塊 -->
                    <div class="score-box">
                        <div class="top">
                            <div class="pic_block">
                                <img alt="商品圖片" src="/product/showPic?prodno=${product.prodNo}">
								<input type="hidden"  class="prodno" name="prodno" value="${product.prodNo}">
                            </div>
                            <div class="star_block">
                                <div class="star_up">
                                    <p>${product.prodName}</p>
                                </div>
								<div class="star_down">
								    <span class="star" data-star="1"><i class="fas fa-star"></i></span>
								    <span class="star" data-star="2"><i class="fas fa-star"></i></span>
								    <span class="star" data-star="3"><i class="fas fa-star"></i></span>
								    <span class="star" data-star="4"><i class="fas fa-star"></i></span>
								    <span class="star" data-star="5"><i class="fas fa-star"></i></span>
								</div>
								<input type="hidden" class="rating-input" value="0">
                            </div>
                        </div>
                        <div class="bottom">
                            <div class="content_block">
                                <textarea placeholder="請輸入您的評價..."></textarea>
                            </div>
                        </div>
                    </div>
                `;
                modalContent.append(score_block);
            });
			const submit_btn =$("#ratingModal").find("#submitRating");
			submit_btn.css("display","block");
            // 顯示燈箱
            $("#ratingModal").modal("show");
        })
        .catch((err) => {
            console.error("Fetch error:", err);
        });
});

//提交評論，發送評論到後端，進行update
$("div.order-container").on("click", "button.submit-comment", function () {
    const self = this; // 保存當前按鈕上下文
   const orderNo =$(self).closest("div.order-card").find("#ratingForm").find("input").val();
	
	console.log("更新後的訂單編號:"+$(self).closest("div.order-card").find("#ratingForm").find("input").val());	
	
	
    let isValid = true; // 驗證標誌
    const comments = []; // 用於存儲評論的 DTO 集合

    // 遍歷每個商品的評論區塊
    $("#ratingModal").find(".score-box").each(function () {
            const prodNo = $(this).find("input.prodno").val(); // 商品編號
            const rateScore = $(this).find("input.rating-input").val(); // 評分值
            const rateContent = $(this).find("textarea").val().trim(); // 評論內容

            // 檢查評分是否必填
            if (!rateScore || rateScore === "0") {
                isValid = false;
                alert("請為所有商品選擇評分！");
                return false; // 中斷 each 循環
            }

            // 將商品數據封裝成 DTO
            comments.push({
                orderNo: parseInt(orderNo, 10), // 訂單編號
                prodNo: parseInt(prodNo, 10), // 商品編號
                rateScore: parseInt(rateScore, 10), // 評分值
                rateContent: rateContent || null, // 評論內容，可空
            });
        });

    if (!isValid) return; // 如果驗證未通過，阻止提交
	console.log(JSON.stringify(comments));
  
    fetch(`/order/mem_order/addComment`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(comments), // 傳送評論集合
    })
        .then((res) => {
            if (!res.ok) {
                throw new Error(`HTTP error! Status: ${res.status}`);
            }
            return res.json();
        })
        .then((data) => {
            console.log("評論提交成功，返回數據:", data);
			alert("評價已提交成功!");
			
            // 更新按鈕樣式和文字
            $(self).text("查看評論").removeClass("score-order").addClass("view-comment");

            // 自動關閉燈箱
            $("#ratingModal").modal("hide");
			
			$("a.order-finished").click();
        })
        .catch((err) => {
            console.error("提交評論時出錯:", err);
        });
});



//查看評論
$("div.order-container").on("click", "button.view-comment", function () {
	const self = this; // 保存當前按鈕的上下文
	let orderNoString = $(self).closest("div.order-card").find("p.order-number").text();
	let orderNo = orderNoString.substring(8, orderNoString.length); // 擷取出訂單編號

	  console.log("訂單編號:", orderNo);
	  console.log(typeof orderNo);
	
	  fetch(`/order/mem_order/viewComment?orderNo=${orderNo}`, {
	        method: 'POST',
	        headers: {
	            'Content-Type': 'application/json',
	        },
	    })
	        .then((res) => {
	            if (!res.ok) {
	                throw new Error(`HTTP error! Status: ${res.status}`);
	            }
	            return res.json();
	        })
	        .then((data) => {
	            console.log("返回的數據:", data); // 調試：檢查後端返回的數據
	            // 獲取全局燈箱的內容區域
				  // 填充燈箱內容
				    const modalContent = $("#ratingModal").find("form#ratingForm");
				    modalContent.empty();
				
				    data.forEach((product) => {
				        let score_block = `
				            <div class="score-box">
				                <div class="top">
				                    <div class="pic_block">
				                        <img alt="商品圖片" src="/product/showPic?prodno=${product.prodNo}">
				                    </div>
				                    <div class="star_block">
				                        <div class="star_up">
				                            <p>${product.prodName}</p>
				                        </div>
				                        <div class="star_down">
				                            ${[1, 2, 3, 4, 5]
				                                .map(
				                                    (star) =>
				                                        `<span class="star ${
				                                            product.rateScore >= star ? "-on" : ""
				                                        }" data-star="${star}">
				                                            <i class="fas fa-star"></i>
				                                        </span>`
				                                )
				                                .join("")}
				                        </div>
				                    </div>
				                </div>
				                <div class="bottom">
				                    <div class="content_block">
				                        <p>${product.rateContent || "未提供評論"}</p>
				                    </div>
				                </div>
				            </div>
				        `;
				        modalContent.append(score_block);
				    });
					const submit_btn =$("#ratingModal").find("#submitRating");
					submit_btn.css("display","none");
				    // 顯示燈箱
				    $("#ratingModal").modal("show");
	        })
	        .catch((err) => {
	            console.error("Fetch error:", err);
	        });
	  
			
  
});






//查詢不同狀態的訂單->已完成(orderState=3)
$("a.order-finished").on("click",function(){
	$("ul.tab_block a").addClass("inactive");
	$("a.order-finished").removeClass("inactive");
	$("a.order-finished").addClass("active");
	
	$("div.order-container").html("");	
	
	let orderState =3;
	fetch(`/order/member/orderState/login/${orderState}`, {
			     method: 'GET',
			     headers: {
			         'Content-Type': 'application/json', 
			     },
			 }).then(res => {
	             if (!res.ok) {
	                 throw new Error(`HTTP error! Status: ${res.status}`);
	             }
	             return res.json();
	         })
			 .then(data=>{
				if(data.length!=0){
					//取得orderSummary(DTO list)，迭代每個訂單、資訊放到html之上    
							       	data.forEach(order=>{
										let buttonText = order.comment == true ? "查看評論" : "評價商品";
										let buttonClass = order.comment == true  ? "view-comment" : "score-order";
										
										let insertHTML =`
															<div class="order-card">		        		
													        	<div class="order-info">
													        		<p class="order-number">訂單編號: 00${order.orderNo}</p>      		
													        		<div class="total-price">
														        		<p class="price-title" >訂單總金額</p>
														        		<p class="total">${order.total}</p>
													        		</div>
													        		
													        	</div>
													        		
													        	<div class="order-item">
													        		<div class="product-image">
													        			<img alt="產品圖像" src="/product/showPic?prodno=${order.prodNo}">
													        		</div>
													        		<div class="product-name">
													        			<p>[ ${order.prodName} ]</p>
													        		</div>		        			                 
												                    <div class="price-container">
													                    <p class="nubmer">${order.quantity}</p>
													                    <p class="price">${order.price}</p>
												                    </div>                    
													        	</div>
													        		
													        	 <div class="order-detail">
													        		<div class="empty-div"></div>
													        		<div class="check-detail">
													        			<a class="check-detail" style="margin-left:20px;" href="/order/mem_order/detail?orderNo=${order.orderNo}">查看訂單詳情 >></a>
													        		</div>		        	 	
													        	 	<div class="cancel-button" style="flex:1.2;">
													        	 		<input type="hidden">
																		
																		 <!-- 評價按鈕 -->
																		<button type="button" class="btn ${buttonClass}">
														                    ${buttonText}
														                </button>
																	
																	    <!-- Modal -->
																	    <div class="modal fade" id="ratingModal" tabindex="-1" aria-labelledby="ratingModalLabel" aria-hidden="true">
																	        <div class="modal-dialog">
																	            <div class="modal-content">
																	                <div class="modal-header">
																	                    <h5 class="modal-title" id="ratingModalLabel">商品評價</h5>
																	                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
																	                </div>
																	                <div class="modal-body">
																					<div class="error-msg text-danger"></div> 
																	                    <!-- 評價表單 -->
																	                    <form id="ratingForm" >
																							<input type="hidden" value="${order.orderNo}">
																	                                      
																	                   
																	                    </form>
																	                </div>
																	                <div class="modal-footer">
																	                    <!-- data-bs-dismiss="modal"加上此屬性可以再點擊後讓燈箱關閉 -->
										<!-- 							                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" style="background-color:rgb(180, 180, 180);color:white;">取消</button> -->
																						<input type="hidden">
																	                    <button type="submit" class="btn btn-primary submit-comment" id="submitRating"   style="background-color:#1e6bd5;color:white;">提交評價</button>
																	                </div>
																	            </div>
																	        </div>
																	    </div>	
										
																		
																		
													        	 		<button class="btn " style="background-color:#ed0540;color:white;">再次購買</button>
													        	 	</div>		        	 	
													                
													        	</div>	          			     		            
												    			 </div>`;
																 
												$("div.order-container").append(insertHTML);									
									})
					}else{
						addDefaultInfo();
					}
				
				
		       })
	
});


//查詢不同狀態的訂單->已取消(orderState=1)
$("a.order-canceled").on("click",function(){
	$("ul.tab_block a").addClass("inactive");
	$("a.order-canceled").removeClass("inactive");
	$("a.order-canceled").addClass("active");
	
	$("div.order-container").html("");	
	
	let orderState =1;
	fetch(`/order/member/orderState/login/${orderState}`, {
			     method: 'GET',
			     headers: {
			         'Content-Type': 'application/json', 
			     },
			 }).then(res => {
	             if (!res.ok) {
	                 throw new Error(`HTTP error! Status: ${res.status}`);
	             }
	             return res.json();
	         })
			 .then(data=>{
				if(data.length!=0){
					//取得orderSummary(DTO list)，迭代每個訂單、資訊放到html之上    
							       	data.forEach(order=>{
										let insertHTML =`
															<div class="order-card">		        		
													        	<div class="order-info">
													        		<p class="order-number">訂單編號: 00${order.orderNo}</p>      		
													        		<div class="total-price">
														        		<p class="price-title" >訂單總金額</p>
														        		<p class="total">${order.total}</p>
													        		</div>
													        		
													        	</div>
													        		
													        	<div class="order-item">
													        		<div class="product-image">
													        			<img alt="產品圖像" src="/product/showPic?prodno=${order.prodNo}">
													        		</div>
													        		<div class="product-name">
													        			<p>[ ${order.prodName} ]</p>
													        		</div>		        			                 
												                    <div class="price-container">
													                    <p class="nubmer">${order.quantity}</p>
													                    <p class="price">${order.price}</p>
												                    </div>                    
													        	</div>
													        		
													        	 <div class="order-detail">
													        		<div class="empty-div"></div>
													        		<div class="check-detail">
													        			<a class="check-detail" href="/order/mem_order/detail?orderNo=${order.orderNo}">查看訂單詳情 >></a>
													        		</div>		        	 	
													        	 	<div class="cancel-button">
													        	 		<input type="hidden">
													        	 		<button class="btn" style="background-color:#ed0540;color:white;">重新購買</button>
													        	 	</div>		        	 	
													                
													        	</div>	          			     		            
												    			 </div>`;
																 
												$("div.order-container").append(insertHTML);
												
									});	
						}else{
							addDefaultInfo();
						}
				
				
		       })
	
});

//查詢不同狀態的訂單->申請退貨(orderState=2)
$("a.return-goods").on("click",function(){
	$("ul.tab_block a").addClass("inactive");
	$("a.return-goods").removeClass("inactive");
	$("a.return-goods").addClass("active");
	
	$("div.order-container").html("");	
	
	let orderState =2;
	fetch(`/order/member/orderState/login/${orderState}`, {
			     method: 'GET',
			     headers: {
			         'Content-Type': 'application/json', 
			     },
			 }).then(res => {
	             if (!res.ok) {
	                 throw new Error(`HTTP error! Status: ${res.status}`);
	             }
	             return res.json();
	         })
			 .then(data=>{
				if(data.length!=0){
					//取得orderSummary(DTO list)，迭代每個訂單、資訊放到html之上    
							       	data.forEach(order=>{
										let insertHTML =`
															<div class="order-card">		        		
													        	<div class="order-info">
													        		<p class="order-number">訂單編號: 00${order.orderNo}</p>      		
													        		<div class="total-price">
														        		<p class="price-title" >訂單總金額</p>
														        		<p class="total">${order.total}</p>
													        		</div>
													        		
													        	</div>
													        		
													        	<div class="order-item">
													        		<div class="product-image">
													        			<img alt="產品圖像" src="/product/showPic?prodno=${order.prodNo}">
													        		</div>
													        		<div class="product-name">
													        			<p>[ ${order.prodName} ]</p>
													        		</div>		        			                 
												                    <div class="price-container">
													                    <p class="nubmer">${order.quantity}</p>
													                    <p class="price">${order.price}</p>
												                    </div>                    
													        	</div>
													        		
													        	 <div class="order-detail">
													        		<div class="empty-div"></div>
													        		<div class="check-detail">
													        			<a class="check-detail" href="/order/admin/statistic/detail/${order.orderNo}">查看退貨明細 >></a>
													        		</div>		        	 	
													        	 	<div class="cancel-button">
																		<input type="hidden" name="orderNo" value="${order.orderNo}">
																		<button type="button" class="btn submit-finished" style="background-color:#ed0540;color:white;">完成訂單</button>
													        	 	</div>		        	 	
													                
													        	</div>	          			     		            
												    			 </div>`;
																 
												$("div.order-container").append(insertHTML);
											
									})
						}else{
							addDefaultInfo();
						}
				
				
		       })
	
});


//查詢不同物流狀態的訂單->待出貨(shipStatus=0)
$("a.delivery").on("click",function(){
	$("ul.tab_block a").addClass("inactive");
	$("a.delivery").removeClass("inactive");
	$("a.delivery").addClass("active");
	
	$("div.order-container").html("");	
	
	let shipStatus =0;

	fetch(`/order/member/shipStatus/login/${shipStatus}`, {
			     method: 'GET',
			     headers: {
			         'Content-Type': 'application/json', 
			     },
			 }).then(res => {
	             if (!res.ok) {
	                 throw new Error(`HTTP error! Status: ${res.status}`);
	             }
	             return res.json();
	         })
			 .then(data=>{
				//取得orderSummary(DTO list)，迭代每個訂單、資訊放到html之上  
			  if(data.length!=0){
				data.forEach(order=>{
									let insertHTML =`
														<div class="order-card">		        		
												        	<div class="order-info">
												        		<p class="order-number">訂單編號: 00${order.orderNo}</p>      		
												        		<div class="total-price">
													        		<p class="price-title" >訂單總金額</p>
													        		<p class="total">${order.total}</p>
												        		</div>
												        		
												        	</div>
												        		
												        	<div class="order-item">
												        		<div class="product-image">
												        			<img alt="產品圖像" src="/product/showPic?prodno=${order.prodNo}">
												        		</div>
												        		<div class="product-name">
												        			<p>[ ${order.prodName} ]</p>
												        		</div>		        			                 
											                    <div class="price-container">
												                    <p class="nubmer">${order.quantity}</p>
												                    <p class="price">${order.price}</p>
											                    </div>                    
												        	</div>
												        		
												        	 <div class="order-detail">
												        		<div class="empty-div"></div>
												        		<div class="check-detail">
												        			<a class="check-detail" href="/order/mem_order/detail?orderNo=${order.orderNo}">查看訂單詳情 >></a>
												        		</div>		        	 	
												        	 	<div class="cancel-button">
												        	 		<input type="hidden" name="cancel-order-no" value="${order.orderNo}">
																	<button class="btn cancel-order" style="background-color:#1e6bd5;color:white;">取消</button>
												        	 	</div>		        	 	
												                
												        	</div>	          			     		            
											    			 </div>`;
															 
											$("div.order-container").append(insertHTML);											
								})
			 			 }else{
							addDefaultInfo();
						 }
		       	
				
		       })
	
});


//查詢不同物流狀態的訂單->待收貨(shipStatus=2)
$("a.receiving").on("click",function(){
	$("ul.tab_block a").addClass("inactive");
	$("a.receiving").removeClass("inactive");
	$("a.receiving").addClass("active");
	
	$("div.order-container").html("");	
	
	let shipStatus =2;

	fetch(`/order/member/shipStatus/login/${shipStatus}`, {
			     method: 'GET',
			     headers: {
			         'Content-Type': 'application/json', 
			     },
			 }).then(res => {
	             if (!res.ok) {
	                 throw new Error(`HTTP error! Status: ${res.status}`);
	             }
	             return res.json();
	         })
			 .then(data=>{
				if(data.length!=0){
					//取得orderSummary(DTO list)，迭代每個訂單、資訊放到html之上    
							       	data.forEach(order=>{
										
										let insertHTML =`
															<div class="order-card">		        		
													        	<div class="order-info">
													        		<p class="order-number">訂單編號: 00${order.orderNo}</p>      		
													        		<div class="total-price">
														        		<p class="price-title" >訂單總金額</p>
														        		<p class="total">${order.total}</p>
													        		</div>
													        		
													        	</div>
													        		
													        	<div class="order-item">
													        		<div class="product-image">
													        			<img alt="產品圖像" src="/product/showPic?prodno=${order.prodNo}">
													        		</div>
													        		<div class="product-name">
													        			<p>[ ${order.prodName} ]</p>
													        		</div>		        			                 
												                    <div class="price-container">
													                    <p class="nubmer">${order.quantity}</p>
													                    <p class="price">${order.price}</p>
												                    </div>                    
													        	</div>
													        		
													        	 <div class="order-detail">
													        		<div class="empty-div"></div>
													        		<div class="check-detail">
													        			<a class="check-detail" href="/order/mem_order/detail?orderNo=${order.orderNo}">查看訂單詳情 >></a>
													        		</div>		        	 	
													        	 	<div class="cancel-button">
																		<form action="/order/member/return" method="post">
													        	 			<input type="hidden" name="orderNo" value="${order.orderNo}">
																			<button type="submit" class="btn return-goods" style="background-color:#1e6bd5;color:white;margin-right:15px;">退貨</button>
																		</form>
																		<button type="button" class="btn submit-finished" style="background-color:#ed0540;color:white;">完成訂單</button>
													        	 	</div>		        	 	
													                
													        	</div>	          			     		            
												    			 </div>`;
																 
												$("div.order-container").append(insertHTML);
												checkAndShowNoOrdersMessage();
									})
						}else{
							addDefaultInfo();
						}
				
				
		       })
	
});


//回傳insertHTML的方法

function addhtml(order){
	let insertHTML =`
					<div class="order-card">		        		
			        	<div class="order-info">
			        		<p class="order-number">訂單編號: 00${order.orderNo}</p>      		
			        		<div class="total-price">
				        		<p class="price-title" >訂單總金額</p>
				        		<p class="total">${order.total}</p>
			        		</div>
			        		
			        	</div>
			        		
			        	<div class="order-item">
			        		<div class="product-image">
			        			<img alt="產品圖像" src="/product/showPic?prodno=${order.prodNo}">
			        		</div>
			        		<div class="product-name">
			        			<p>[ ${order.prodName} ]</p>
			        		</div>		        			                 
		                    <div class="price-container">
			                    <p class="nubmer">${order.quantity}</p>
			                    <p class="price">${order.price}</p>
		                    </div>                    
			        	</div>
			        		
			        	 <div class="order-detail">
			        		<div class="empty-div"></div>
			        		<div class="check-detail">
			        			<a class="check-detail" href="/order/mem_order/detail?orderNo=${order.orderNo}">查看訂單詳情 >></a>
			        		</div>		        	 	
			        	 	<div class="cancel-button">
			        	 		<input type="hidden">
			        	 		
			        	 	</div>		        	 	
			                
			        	</div>	          			     		            
		    			 </div>`;
						 
		$("div.order-container").append(insertHTML);
		
		return insertHTML;
}


function addHtmlWithRedBtn(order){
	let insertHTML =`
					<div class="order-card">		        		
			        	<div class="order-info">
			        		<p class="order-number">訂單編號: 00${order.orderNo}</p>      		
			        		<div class="total-price">
				        		<p class="price-title" >訂單總金額</p>
				        		<p class="total">${order.total}</p>
			        		</div>
			        		
			        	</div>
			        		
			        	<div class="order-item">
			        		<div class="product-image">
			        			<img alt="產品圖像" src="/product/showPic?prodno=${order.prodNo}">
			        		</div>
			        		<div class="product-name">
			        			<p>[ ${order.prodName} ]</p>
			        		</div>		        			                 
		                    <div class="price-container">
			                    <p class="nubmer">${order.quantity}</p>
			                    <p class="price">${order.price}</p>
		                    </div>                    
			        	</div>
			        		
			        	 <div class="order-detail">
			        		<div class="empty-div"></div>
			        		<div class="check-detail">
			        			<a class="check-detail" href="/order/mem_order/detail?orderNo=${order.orderNo}">查看訂單詳情 >></a>
			        		</div>		        	 	
			        	 	<div class="cancel-button">
			        	 		<input type="hidden">
			        	 		<button class="btn" style="background-color:#ed0540;color:white;">再次購買</button>
			        	 	</div>		        	 	
			                
			        	</div>	          			     		            
		    			 </div>`;
						 
		$("div.order-container").append(insertHTML);
}

	   
document.querySelector("div.order-container").addEventListener("click", (event) => {
    const clickedStar = event.target.closest(".star"); // 確保點擊的是 .star 或其子元素

    if (clickedStar) {
        // 找到這一組的所有星星
        const allStars = clickedStar.parentElement.querySelectorAll(".star");
        const index = Array.from(allStars).indexOf(clickedStar); // 找到被點擊星星的索引 (0 ~ 4)

        // 高亮處理：將點擊星星及之前的星星設為高亮
        for (let i = 0; i <= index; i++) {
            allStars[i].classList.add("-on");
        }

        // 恢復處理：將點擊星星之後的星星取消高亮
        for (let i = index + 1; i < allStars.length; i++) {
            allStars[i].classList.remove("-on");
        }

        // 更新當前評分值到隱藏的 input
        const starBlock = clickedStar.closest(".star_block"); // 找到當前的星星區塊
        const ratingInput = starBlock.querySelector(".rating-input"); // 找到隱藏的 input
        const selectedStar = index + 1; // 評分值等於點擊的索引 + 1
        ratingInput.value = selectedStar; // 將選擇的評分值存入隱藏的 input

        console.log("商品評分值已更新:", selectedStar); // 調試用，打印出當前選擇的評分值
    }
});




	   
	   
// 父容器事件委託，監聽動態生成的取消按鈕
$("div.order-container").on("click", "button.cancel-order", function () {
    let is_confirm = confirm("確定要取消訂單嗎?");

    if (is_confirm) {
        // 獲取訂單編號
        let orderNo = $(this).closest("div.cancel-button").find("input").val();
        let sendNo = parseInt(orderNo); // 將訂單號轉為整數
        console.log(orderNo);
        console.log("Type:", typeof sendNo); 
        console.log(`/order/member/${sendNo}/cancel`);

        // 發送PUT請求取消訂單
        fetch(`/order/member/${sendNo}/cancel`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        .then(res => {
            if (!res.ok) {
                throw new Error(`HTTP error! Status: ${res.status}`);
            }
            return res.json();
        })
        .then(data => {
            if (data.success) {
                $(this).text("重新訂購"); // 修改按鈕文本
                $(this).css("background-color", "#ed0540"); // 修改按鈕背景色
				$(this).css("color", "white");
                alert("您已成功取消訂單!");
            } else {
                alert("取消訂單失敗!");
            }
        })
        .catch(err => {
            console.error("取消訂單時出錯:", err);
        });
    }
});


// 父容器事件委託，監聽動態生成的完成訂單按鈕
$("div.order-container").on("click", "button.submit-finished", function () {
    let is_confirm = confirm("確定要送出訂單完成嗎?");

    if (is_confirm) {
        // 獲取訂單編號
        let orderNo = $(this).closest("div.cancel-button").find("input").val();
        let sendNo = parseInt(orderNo); // 將訂單號轉為整數
        console.log(orderNo);
        console.log("Type:", typeof sendNo); 
        console.log(`/order/member/${sendNo}/cancel`);

        // 發送PUT變更訂單狀態為完成
        fetch(`/order/member/${sendNo}/finished`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        .then(res => {
            if (!res.ok) {
                throw new Error(`HTTP error! Status: ${res.status}`);
            }
            return res.json();
        })
        .then(data => {
            if (data.success) {             
                alert("訂單完成!");
//				$(this).closest("div.order-card").remove();			
				
            } else {
                alert("無法傳送訂單完成!");
            }
        })
        .catch(err => {
            console.error("取消訂單時出錯:", err);
        });
    }
});


	   
	   
	   //綁定評價區塊的送出按紐，fetch 更新的API
//	$("#submitRating").on("click",function(){
//		//取得評分數、取得評分內容
//		
//		fetch(`/order/mem_order/addCommet`, {
//					     method: 'POST',
//					     headers: {
//					         'Content-Type': 'application/json', 
//					     },
//						 
//					 }).then(res => {
//			             if (!res.ok) {
//			                 throw new Error(`HTTP error! Status: ${res.status}`);
//			             }
//			             return res.json();
//			         })
//					 .then(data=>{
//						
//							
//						})
//						
//				       })
//		
//		
//	});   

$(document).ready(function () {
    checkAndShowNoOrdersMessage(); // 頁面加載完成後檢查
});
	   

// 定義檢查訂單數量並顯示提示文字的函數
function checkAndShowNoOrdersMessage() {
    if ($("div.order-container").children().length === 0) {
        $("div.order-container").html(
            `			
			<div style="margin:100px 0 80px 0;text-align:center;">
				<p>尚未有任何訂單</p>
			</div>	
			`
        );
    }
}

//預設無訂單的顯示文字
function addDefaultInfo(){
	let replaceHTML =`
						<div style="margin:100px 0 80px 0;text-align:center;">
							<p>尚未有任何訂單</p>
						</div>	
					`;
					
					$("div.order-container").append(replaceHTML);
//					$("div.order-container").css("background-color","#ccc")
					
}


