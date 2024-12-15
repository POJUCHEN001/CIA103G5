
//清空表單
	$("form").on("reset",function(){
		$("div.form-divide input").val("");
		$("div.form-divide textarea").val("");
		$("div.form-divide p").text("");
		
		$("div.drop_zone").html("");
		$("div.drop_zone").html("<span class='text'>預覽圖</span>");		
	});




// <<表單>>輸入內容進行錯誤處理

$("button.add ").on("click", function(e) {
	
	//商品名稱
	let regex = /^[\u4e00-\u9fa5a-zA-Z0-9]+$/;
	let name =$("input.product-name").val().trim();
	
	if(name==""){
		e.preventDefault();
		$("div.name p").text("請輸入商品名稱");
	}else if(name!="" && regex.test(name)==false){
		e.preventDefault();
		$("div.name p").text("請勿輸入符號，僅可輸入中文、英文或數字");
	}else{
		$("div.name p").text("");
	}
	
	//價格
	let price =$("input.product-price").val().trim();
	if(price==""){
			e.preventDefault();
			$("div.price p").text("請輸入商品價格");
		}else if(price!=""&&parseInt(price)<=0){
			e.preventDefault();
			$("div.price p").text("商品價格不可小於等於0");
		}else{
			$("div.price p").text("");
		}
		
	//庫存	
	let number =$("input.product-quantity").val().trim();
	if(number==""){
			e.preventDefault();
			$("div.number p").text("請輸入商品庫存數量");
		}else if(number!="" && parseInt(number)<0){
			e.preventDefault();
			$("div.number p").text("商品不可為負數");
		}else{
			$("div.number p").text("");
		}	

	//商品資訊
	let info =$("textarea.product-info").val().trim();
	if(info==""){
			e.preventDefault();
			$("div.info p").text("請輸入商品資訊");
		}else if(info.length > 250) {
		        e.preventDefault();
		        $("div.info p").text("商品資訊字數已達上限，輸入不可超過250個字");
		        $("textarea.product-info").val(info.substring(0, 251));
		    }else{
			$("div.info p").text("");
		}
	
	//圖片
	let img =$("input.product-img").val().trim();
	if(img==""){
		e.preventDefault();
			$("div.img p").text("請上傳一張商品照片");
		}else{
			$("div.img p").text("");
		}	

			
});

	//<<即時動態檢查字數>>
	//檢查商品資訊的字數上限:使用者輸入資訊框時，觸發input事件，隨即檢查字數
	$("textarea.product-info").on("input",function(){
		
		let prodinfo =$("textarea.product-info").val().trim();
			
			if(prodinfo.length>250){
				$("div.info p").text("商品資訊字數已達上限，字數不可超過250個字");	
				$("textarea.product-info").val(prodinfo.substring(0,251));			
			}else{
				$("div.info p").text("");	
			}

	});
	

//點擊側邊欄符號，滑動縮放側邊欄
    let isSidebarOpen = true;

    $("i.fa-bars").on("click",function () {
        if (isSidebarOpen) {
            $("aside.sidebar").animate({ width: "50px" }, 300);
            $("aside.sidebar ul,aside.sidebar h5").hide();
            $(this).animate({ right: "15px" }, 300);
        } else {
            $("aside.sidebar").animate({ width: "250px" }, 300);
            $("aside.sidebar ul,aside.sidebar h5").show();
            $(this).animate({ right: "5px" }, 300);
        }
        isSidebarOpen = !isSidebarOpen;
    });


	//串狀態查詢API
	$("select.choose-status").on("change",function(){
		let statusString =$("select.choose-status").val();
			
		if(statusString!=""){
			let status=statusString=="上架"?1 :0;
			console.log(status);
			
			// 發送 AJAX 請求到後端 API
			       fetch('/product/query/status', {
			           method: 'POST',
			           headers: {
			               'Content-Type': 'application/x-www-form-urlencoded',
			           },
			           body: `status=${status}`, // 將參數以 key=value 形式發送
			       }).then((res) => {
				              if (!res.ok) {
				                   throw new Error(`HTTP error! Status: ${res.status}`);
				               }
				               return res.json();
				           }).then(res => {
								$("tbody.table-group-divider").empty();
								res.data.forEach((productVO)=>{
									let tr =`
									<tr>
										<td>${productVO.prodNo}</td>
										<td>
											<img src="/product/showPic?prodno=${productVO.prodNo}"  style="width:80px;height:80px" class="photo" name="photo" >
										</td>
										<td>${productVO.prodName}</td>
										<td>${productVO.price}</td>									
										<td>${productVO.availableQuantity}</td>
										<td>${productVO.status==1?'上架':'下架'}</td>
										<td>${productVO.prodDesc}</td>
										<td>${res.time[productVO.prodNo]}</td> <!-res裡面找到time屬性，裡面有所有的建立時間，用prodno作為主鍵取出->
										<td>
											<form method="post" action="/product/update_product">
												<input type="hidden" name="prodno" value="${productVO.prodNo}">				               
												<button type="submit" class="btn update" style="background-color:#1e6bd5;color:white;">修改</button>
											</form>
										</td>
										<td>
										<form method="post" action="/product/delete_product">
											<input type="hidden" name="prodno" value="${productVO.prodNo}">
											<button type="button" class="btn delete" style="background-color:#ed0540;color:white;" data-bs-toggle="modal" data-bs-target="#Modal1">刪除</button>
												
											<!-- Modal -->
											<div class="modal fade" id="Modal1" tabindex="-1" aria-labelledby="Modal1" aria-hidden="true">
											  <div class="modal-dialog">
											    <div class="modal-content">						
											      <div class="modal-body">
											      	<div style="margin:30px 0;">
											      		 確認要刪除該商品嗎?	
											      	</div>									       
											        <button type="button" class="btn btn-primary yes" style="background-color:#007bff;margin-right:10px;">確定</button>
											        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" style="background-color:#ebebeb;color:#007bff;">取消</button>
											      </div>
										
											    </div>
											  </div>
											</div>
										</form>
									</td>
									</tr>
									`;
									
									$("tbody.table-group-divider").append(tr);
								});
			           }).catch(error => {
			               console.error("Error:", error.message);
			               $("#result").html(`<p style="color:red;">${error.message}</p>`);
			           });
			   } 
		
		
	});
	
	//串複合查詢API
	$("#query_product").on("click",function(){
		let statusString =$("select.choose-status").val();
		let prodName =$("#qy-name").val().trim();
		let prodNo =$("#qy-no").val();
		
		let status;	
		if(statusString=="上架"){
			status=1;
		}else if(statusString=="下架"){
			status=0;
		}else if(statusString==""){
			status=null;
		}
			
		let queryData = {
		      status: status ? status.toString() : null, // 確保傳的是字符串
		      prodName: prodName || null, // 空字符串轉成 null
		      prodNo: prodNo || null // 空字符串轉成 null
		  };
		  
		  console.log(queryData)
			    fetch('/product/query/composite', {
			           method: 'POST',
			           headers: {
			               'Content-Type': 'application/json',
			           },
			           body: JSON.stringify(queryData) 
			       }).then((res) => {
				              if (!res.ok) {
				                   throw new Error(`HTTP error! Status: ${res.status}`);
				               }
				               return res.json();
				           }).then(res => {
							
								if (res.isEmpty) {
									alert("查無資料，請重新輸入查詢條件");
						          return;
						      	}
							
								$("tbody.table-group-divider").empty();
																
								res.data.forEach((productVO)=>{
									let tr =`
									<tr>
										<td>${productVO.prodNo}</td>
										<td>
											<img src="/product/showPic?prodno=${productVO.prodNo}"  style="width:80px;height:80px" class="photo" name="photo" >
										</td>
										<td>${productVO.prodName}</td>
										<td>${productVO.price}</td>									
										<td>${productVO.availableQuantity}</td>
										<td>${productVO.status==1?'上架':'下架'}</td>
										<td>${productVO.prodDesc}</td>
										<td>${res.time[productVO.prodNo]}</td> <!-res裡面找到time屬性，裡面有所有的建立時間，用prodno作為主鍵取出->
										<td>
											<form method="post" action="/product/update_product">
												<input type="hidden" name="prodno" value="${productVO.prodNo}">				               
												<button type="submit" class="btn update" style="background-color:#1e6bd5;color:white;">修改</button>
											</form>
										</td>
										<td>
										<form method="post" action="/product/delete_product">
											<input type="hidden" name="prodno" value="${productVO.prodNo}">
											<button type="button" class="btn delete" style="background-color:#ed0540;color:white;" data-bs-toggle="modal" data-bs-target="#Modal2">刪除</button>
											<!-- Modal -->
													<div class="modal fade" id="Modal2" tabindex="-1" aria-labelledby="Modal2" aria-hidden="true">
													  <div class="modal-dialog">
													    <div class="modal-content">						
													      <div class="modal-body">
													      	<div style="margin:30px 0;">
													      		 確認要刪除該商品嗎?	
													      	</div>									       
													        <button type="button" class="btn btn-primary yes" style="background-color:#007bff;margin-right:10px;">確定</button>
													        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" style="background-color:#ebebeb;color:#007bff;">取消</button>
													      </div>
												
													    </div>
													  </div>
													</div>
											</form>
										</td>
									</tr>
									`;
									
									$("tbody.table-group-divider").append(tr);
								});
			           }).catch(error => {
			               console.error("Error:", error.message);
							alert("查無資料，請重新輸入查詢條件");
			           });
			   
		
		
	});
	
	
	//刪除的二次確認
//	document.addEventListener("click", function (event) { 
//		  
//		if(event.target.classList.contains("yes")){
//			let prodNo = event.target.closest("form").querySelector("input").value;
//			console.log(prodNo); 
//			const formToSubmit =event.target.closest("form");
//			console.log(formToSubmit);
//				//formToSubmit.submit();
//				
//			}else{
//				event.preventDefault();
//			}
//	    
//	});

	// 全局監聽刪除按鈕點擊事件
	$(document).on("click", ".btn.delete", function () {
	    // 找到當前點擊的刪除按鈕，對應的 modal
	    let modalId = $(this).data("bs-target"); // 取得 data-bs-target 對應的 modal ID
	    let form = $(this).closest("form"); // 找到按鈕對應的 form 表單
		let prodNo =form.find("input").val();
		console.log(form);
		console.log(modalId);
		console.log(prodNo);
		
		
	    // 在 modal 確認按鈕上綁定提交事件
	    $(modalId).find(".yes").off("click").on("click", function () {
	        form.submit(); // 提交該表單
	        $(modalId).modal("hide"); // 關閉燈箱
	    });
	
	    // 打開燈箱
	    $(modalId).modal("show");
	});



