
$("#query").on("click",function(){
	//type為字串
	let ftid =$("#qy-ftid").val().trim();
	let memid=$("#qy-name").val().trim();
	let orderno =$("#qy-no").val().trim();
	
	const formData = new URLSearchParams();
	formData.append("orderNo",orderno);
	formData.append("memId",memid);
	formData.append("ftId",ftid);
	
	fetch(`/order/query/composite/byAdmin`, {
	       method: 'POST',
	       headers: {
			'Content-Type': 'application/x-www-form-urlencoded',
	       },
		   body: formData.toString(),
	   })
	       .then((res) => {
	           if (!res.ok) {
	               throw new Error(`HTTP error! Status: ${res.status}`);
	           }
	           return res.json();
	       })
	       .then((res) => {
	           // 清空 Modal 中的表格
//	           $("table#queryOrderTable tbody").empty();
				if(res.data.length!=0){
					$("table#queryOrderTable tbody").empty();
					res.data.forEach((DTO)=>{
						let orderStateColor = "";
						let shipStatusColor = "";
						
						switch (DTO.orderState) {
				            case "已成立": // 已成立
				                orderStateColor = "background-color:white;"; 
				                break;
				            case "已取消": // 已取消
				                orderStateColor = "background-color:rgb(239, 187, 194);"; 
				                break;
				            case "退貨": // 退貨
				                orderStateColor = "background-color:rgb(181, 176, 177);"; 
				                break;
				            case "已完成": // 已完成
				                orderStateColor = "background-color:white;"; 
				                break;
				            default:
				                orderStateColor = "background-color: white;"; 
				        }

				        switch (DTO.shipStatus) {
				            case "待出貨": // 待出貨
				                shipStatusColor = "background-color:rgb(176, 199, 241);"; 
				                break;
				            case "已出貨": // 已出貨
				                shipStatusColor = "background-color:white;"; 
				                break;
				            case "已送達": // 已送達
				                shipStatusColor = "background-color:white;"; 
				                break;
							case "待出貨": //待出貨(已取消)
								shipStatusColor ="background-color:rgb(176, 199, 241);";
								break;
							 case "已送達" ://已送達(退貨)
								shipStatusColor ="background-color:white;";
								break;
							case "已送達" ://已送達(完成)
								shipStatusColor ="background-color:white;";
				            default:
				                shipStatusColor = "background-color: #D3D3D3;"; 
				        }	
						
						
					let tabladata =`
									<tr>
							             <td class="orderno" name="orderno">${DTO.orderNo}</td>					          					             
							             <td class="ftid" name="ftid">${DTO.ftId}</td>	
							             <td class="memid" name="memid">${DTO.memId}</td>						             	            
							       		 <td class="payment" name="payment" >${DTO.payment}</td>						       	
							       		 <td class="orderamount" name="orderamount" >${DTO.orderAmount}</td>							       	
							       		 <td class="realamount" name="realamount">${DTO.realAmount}</td>						       	
							       		 <td class="orderstate" name="orderstate" style="${orderStateColor}">${DTO.orderState}</td>						       	
							       		 <td class="shipstate" name="shipstate" style="${shipStatusColor}">${DTO.shipStatus}</td>						       						       				          	            
							       		 <td class="createdtime" name="createdtime" >${DTO.createdTime}</td>
							       		 <td class="endedtime" name="endedtime">${DTO.endedTime!=null ? DTO.endedTime : '-'}</td>				
							        </tr>
									`;
									
									$("table#queryOrderTable tbody").append(tabladata);	
								
						
					   });
								   
				}else{
					alert("查無資料，請重新輸入!");
					return;
				}
	           
	       })
	       .catch((error) => {
	           console.error("Error fetching order details:", error);
	       });
	
	
	
});



	const shipStatusColor ={
		"待出貨" :"rgb(176, 199, 241)",
		"已出貨" :"white",
		"已送達" :"white",
		"待出貨" :"rgb(176, 199, 241)",
		"已送達" :"white",
		"已送達" :"white"
	}
	
	const orderStateColor ={
		"已成立" :"white",
		"已取消" :"rgb(239, 187, 194)",
		"退貨" :"rgb(181, 176, 177)",
		"已完成" :"white"
	}

	let shipstates =document.querySelectorAll("tbody.table-group-divider td.shipstate");
	let orderStates=document.querySelectorAll("tbody.table-group-divider td.orderstate");
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


