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

$("#statis_query").on("click",function(){
	let year =$("div.year select").val();
	let month=$("div.month select").val();
	console.log(typeof year);
	console.log(typeof month);
	
	const formData = new URLSearchParams();
	formData.append("year",year);
	formData.append("month",month);
	
	
	fetch(`/order/query/chooseDate/byAdmin`, {
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
	           
				if(data.totalCount!=0){
					$("#statisTable tbody").empty();
					let html =`
					<tr>
			             <td class="orders_count" name="orders_count" >${data.totalCount}</td>			            					       	
			       		 <td class="orders_amount" name="orders_amount"  >${data.totalAmount}</td>			          	            			     		
			       		 <td class="revenue" name="revenue" >${data.revenue}</td>
			       		 <td class="settlement" name="settlement" >${data.settlement}</td>			
			       		 <td class="gain" name="gain" style="background-color:rgb(239, 187, 194);">${data.revenue}</td>			
			        </tr>
					`;
					$("#statisTable tbody").append(html);
					
				}else{
					alert("查無資料，請重新輸入!");
				}
			
	       })
	       .catch((error) => {
	           console.error("Error fetching order details:", error);
	       });
	
	
		
});





