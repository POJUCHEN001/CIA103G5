
//$("a.open_modal").on("click", function () {
//    let tabledata = $(this).closest("tr").find("td.orderno");
//    let text = tabledata.text().trim();
//    let orderNo = parseInt(text);
//
//    fetch(`/order/ft_order/detail/${orderNo}`, {
//        method: 'GET',
//        headers: {
//            'Content-Type': 'application/json',
//        },
//    })
//        .then((res) => {
//            if (!res.ok) {
//                throw new Error(`HTTP error! Status: ${res.status}`);
//            }
//            return res.json();
//        })
//        .then((data) => {
//            // 確保清空正確的表格內容
//            $("#ratingModal table.mytable1 tbody").empty();
//
//			// 動態更新訂單金額、積分等資料
//	          $("#total").text(data.order.orderAmount);
//	          $("#point").text(data.order.pointUse);
//	          $("#realpay").text(data.order.realAmount);
//	          $("#note").text(data.order.note);
//			
//            // 動態插入產品資料
//            data.details.forEach((detail) => {
//								
//				let row = `
//                    <tr>
//                        <td>${detail.compositekey.productVO.prodNo}</td>
//                        <td>${detail.compositekey.productVO.prodName}</td>
//                        <td>${detail.price}</td>
//                        <td>${detail.quantity}</td>
//                    </tr>
//                `;
//                $("#ratingModal table.mytable1 tbody").append(row);
//            });
//
//          
//            // 使用 Bootstrap 方法顯示 Modal
//            let modal = new bootstrap.Modal(document.getElementById("ratingModal"));
//            modal.show();
//        })
//        .catch((error) => {
//            console.error("Error fetching order details:", error);
//        });
//});

// 確保 Modal 背景被移除
//document.getElementById("ratingModal").addEventListener('hidden.bs.modal', () => {
//    document.querySelectorAll('.modal-backdrop').forEach((backdrop) => {
//        backdrop.remove();
//    });
//});

document.getElementById("ratingModal").addEventListener('hidden.bs.modal', () => {
    // 清除 `body` 的 class 和樣式
    document.body.className = document.body.className.replace(/\bmodal-open\b/, '');
    document.body.style.overflow = "";
    document.body.style.paddingRight = "";

    // 移除多餘的 backdrop
    document.querySelectorAll('.modal-backdrop').forEach((backdrop) => {
        backdrop.remove();
    });
});



$("body").on("click", "a.open_modal", function (e) {
    e.preventDefault(); // 防止鏈接跳轉
    let orderNo = $(this).data("order-no"); // 獲取訂單號
	
    fetch(`/order/ft_order/detail/${orderNo}`, {
        method: 'GET',
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
            // 清空 Modal 中的表格
            $("#ratingModal table.mytable1 tbody").empty();

            // 動態插入訂單總金額、積分等信息
            $("#total").text(data.order.orderAmount);
            $("#point").text(data.order.pointUse);
            $("#realpay").text(data.order.realAmount);
            $("#note").text(data.order.note);

            // 動態生成產品明細行
            data.details.forEach((detail) => {
                let row = `
                <tr>
                    <td>${detail.compositekey.productVO.prodNo}</td>
                    <td>${detail.compositekey.productVO.prodName}</td>
                    <td>${detail.price}</td>
                    <td>${detail.quantity}</td>
                </tr>
                `;
                $("#ratingModal table.mytable1 tbody").append(row);
            });

            // 使用 Bootstrap 方法顯示 Modal
            let modal = new bootstrap.Modal(document.getElementById("ratingModal"));
            modal.show();
        })
        .catch((error) => {
            console.error("Error fetching order details:", error);
        });
});


$("#FTorderTable").on("click","a.alter_order",function(e){
	
	let is_comfirm =confirm("確定要向買家發出已出貨通知嗎?");
	
	if(!is_comfirm){
		e.preventDefault();
	}
	
});




//串複合查詢API
	$("#composite").on("click",function(){
		let stateString =$("select.order-status").val();
		let shipString =null;
		let memId = $("#memno").val().trim();
		let orderNo =$("#orderno").val().trim();
		let date =$("#qy-date").val();
		
		let startDate =null;
		let endDate =null;
		
		
		if(date!=null || date!=""){
			let dates =date.split("to");
			startDate =dates[0];
			endDate =dates[1];
			
			console.log("startDate : "+startDate);
			console.log("endDate : "+endDate);
		}
				
				
		console.log("shipString : "+shipString);
		console.log("memId : "+memId);
		console.log("orderNo : "+orderNo);
		console.log("date : "+date);
	
		
			
		let orderState;	
		switch(stateString){
			case "已成立":
				orderState =0
			break;
			
			case "已取消":
				orderState =1
			break;
			
			case "已完成":
				orderState=3
			break;
			
			case "退貨" :
				orderState =2
			break;			
			default:
				orderState=null
		}
	
		console.log("orderState : "+orderState);
				
		
//			
		let queryData = {
		      orderState: orderState!=null ? orderState.toString() : null, // 確保傳的是字符串
		      shipStatus:shipString,
			  orderNo:orderNo !=""? orderNo : null,
			  memId:memId !="" ? memId : null,
			  endDate:endDate !=null ? endDate : null,
			  startDate:startDate != ""? startDate : null
		  };
		  
		  console.log(queryData)
			    fetch('/order/query/composite', {
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
							
								$("#FTorderTable tbody").empty();
																
								res.data.forEach((orderVO)=>{
									const orderStateMapping = {
									    0: "已成立",
									    1: "已取消",
									    2: "退貨",
									    3: "已完成",
									};

									const shipStatusMapping = {
									    0: "待出貨",
									    1: "已出貨",
									    2: "已送達",
									    3: "待出貨",
										4: "已送達",
										5: "已送達"
									};
									
									
									const paymentMapping = {
										0 :"信用卡",
										1 :"ATM轉帳",
										2 :"超商繳費",
										3 :"貨到付款"
									};

									let paymentText = paymentMapping[orderVO.payment] || "未知付款狀態";
									let orderStateText = orderStateMapping[orderVO.orderState] || "未知訂單狀態";
									let shipStatusText = shipStatusMapping[orderVO.shipStatus] || "未知物流狀態";

									// 根據訂單狀態和物留狀態設置顏色
									        let orderStateColor = "";
									        let shipStatusColor = "";

									        switch (orderVO.orderState) {
									            case 0: // 已成立
									                orderStateColor = "background-color:white;"; 
									                break;
									            case 1: // 已取消
									                orderStateColor = "background-color:rgb(239, 187, 194);"; 
									                break;
									            case 2: // 退貨
									                orderStateColor = "background-color:rgb(181, 176, 177);"; 
									                break;
									            case 3: // 已完成
									                orderStateColor = "background-color:rgb(250, 204, 104);"; 
									                break;
									            default:
									                orderStateColor = "background-color: white;"; 
									        }

									        switch (orderVO.shipStatus) {
									            case 0: // 待出貨
									                shipStatusColor = "background-color:rgb(176, 199, 241);"; 
									                break;
									            case 1: // 已出貨
									                shipStatusColor = "background-color:rgb(176, 241, 205);"; 
									                break;
									            case 2: // 已送達
									                shipStatusColor = "background-color:white;"; 
									                break;
												case 3://待出貨
													shipStatusColor ="background-color:rgb(176, 199, 241);";
													break;
												 case 4 ://已送達
													shipStatusColor ="background-color:white;";
													break;
												case 5 ://已送達
													shipStatusColor ="background-color:white;";
									            default:
									                shipStatusColor = "background-color:white;"; 
									        }						
									
																			
											
											
									let tr =`
									<tr>
							             <td class="orderno" name="orderno" >${orderVO.orderNo}</td>					          
							             <td class="memid" name="memid">${orderVO.memId}</td>			            
							       		 <td class="payment" name="payment">${paymentText}</td>						       	
							       		 <td class="orderamount" name="orderamount">${orderVO.orderAmount}</td>			          	            
							       		 <td class="shipstatus" name="shipstatus" style="${shipStatusColor}">${shipStatusText}</td>	
							       		 <td class="orderstatus" name="orderstate" style="${orderStateColor}"> 
											${orderStateText}
							       		 </td>
							       		 <td class="createdtime" name="createdtime" >${orderVO.createdTime}</td>
							       		 <td class="endedtime" name="endedtime">${orderVO.endedTime!=null ? orderVO.endedTime : '-'}</td>
							       		 <td><a class="alter_order" href="/order/ft_order/delivery/${orderVO.orderNo}" value="${orderVO.shipStatus}==0 && ${orderVO.orderState}==0 ?'出貨':'' ">${orderVO.shipStatus == 0 && orderVO.orderState==0? '出貨' : ''}</a></td>
							       		 <td ><a href="#" class="open_modal" style="text-decoration:none;" data-bs-toggle="modal" data-bs-target="#ratingModal" data-order-no="${orderVO.orderNo}">查看</a></td>				
							        </tr>
									`;
									
									$("#FTorderTable tbody").append(tr);
								});
			           }).catch(error => {
			               console.error("Error:", error.message);
							alert("查無資料，請重新輸入查詢條件");
			           });
					   		   
		
		
	});



	const shipStatusColor ={
		"待出貨" :"rgb(176, 199, 241)",
		"已出貨" :"rgb(176, 241, 205)",
		"已送達" :"white",
		"待出貨" :"rgb(176, 199, 241)",
		"已送達" :"white",
		"已送達" :"white"
	}
	
	const orderStateColor ={
		"已成立" :"white",
		"已取消" :"rgb(239, 187, 194)",
		"退貨" :"rgb(181, 176, 177)",
		"已完成" :"rgb(250, 204, 104)"
	}

	let shipstates =document.querySelectorAll("tbody.table-group-divider td.shipstatus");
	let orderStates=document.querySelectorAll("tbody.table-group-divider td.orderstatus");
//	console.log(shipstates);
//	console.log(orderStates);
	
	shipstates.forEach(shipstate=>{
		let value =shipstate.innerHTML;
//		console.log(value);
		shipstate.style.backgroundColor =shipStatusColor[value];
	});	
	
	orderStates.forEach(orderstate =>{
		let value = orderstate.innerHTML;
//		console.log(value);
		orderstate.style.backgroundColor =orderStateColor[value];
	});




	