
const currentYear = new Date().getFullYear();
const currentMonth =new Date().getMonth();

let year =document.querySelectorAll("select.year option");
let month =document.querySelectorAll("select.month option");

year.forEach(oneyear=>{
	if(oneyear.value==currentYear){
		oneyear.selected =true;
	}
})

month.forEach(onemonth=>{
	if(onemonth.value==currentMonth){
		onemonth.selected =true;
	}
})

$("#statis_query_ft").on("click",function(){
	let year =$("div.year select").val();
	let month=$("div.month select").val();
	console.log(typeof year);
	console.log(typeof month);
	
	const formData = new URLSearchParams();
	formData.append("year",year);
	formData.append("month",month);
	
	console.log($("#title"));
	$("#title").text(month + " 月份統計");
	
	fetch(`/order/query/chooseDate/byFt`, {
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
	       .then((data) => {
			$("table.two tbody").empty();
				if(data.totalCount!=0){					
					
					let html =`
					<tr >
			            <td class="total" >總筆數</td>
			            <td class="total" style="width:400px;" id="total" >${data.totalCount}</td>            						           
			        </tr>
			        
			         <tr >						         
			            <td class="delivery_fee">總金額</td>		            
			            <td class="delivery_fee" id="point" >${data.totalAmount}</td>			            
					           
			        </tr>
			        
			         <tr >	            
			            <td class="point_discount">平台抽成</td>				
			            <td class="revenue" id="realpay">${data.revenue}</td>
			                     						           
			        </tr>
			        
			         <tr >
			            <td class="payment">獲益</td>           						           
			            <td class="settlement" id="note">${data.settlement}</td>	           						           
			        </tr>
					`;
					$("table.two tbody").append(html);
					
				}else{
					let html =`
										<tr >
								            <td class="total" >總筆數</td>
								            <td class="total" style="width:400px;" id="total" >0</td>            						           
								        </tr>
								        
								         <tr >						         
								            <td class="delivery_fee">總金額</td>		            
								            <td class="delivery_fee" id="point" >0</td>			            
										           
								        </tr>
								        
								         <tr >	            
								            <td class="point_discount">平台抽成</td>				
								            <td class="revenue" id="realpay">0</td>
								                     						           
								        </tr>
								        
								         <tr >
								            <td class="payment">獲益</td>           						           
								            <td class="settlement" id="note">0</td>	           						           
								        </tr>
										`;
										$("table.two tbody").append(html);
				}
			
	       })
	       .catch((error) => {
	           console.error("Error fetching order details:", error);
	       });
	
	
		
});

$("td.revenue").each(function() {
    // 抓取當前元素的文字內容並進行處理
    const truncatedValue = Math.trunc(parseFloat($(this).text()));
    // 更新文字內容
    $(this).text(truncatedValue);
});

$("td.settlement").each(function() {
    // 抓取當前元素的文字內容並進行處理
    const truncatedValue = Math.trunc(parseFloat($(this).text()));
    // 更新文字內容
    $(this).text(truncatedValue);
});

$("#revenue").text(Math.trunc(parseFloat($("#revenue").text())));

$("#settlement").text(Math.trunc(parseFloat($("#settlement").text())));

