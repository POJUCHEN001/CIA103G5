
$("table.table-bordered").on("change","input.choose-item",function(){
	let detailsRow = $(this).closest("tr").next("tr.table-row");

	   if (detailsRow.length) {
	       // 檢查 checkbox 是否被勾選，根據狀態顯示或隱藏詳細行
	       if ($(this).prop("checked")) {
	           detailsRow.css("display","table-row");
			   let inputs =detailsRow.find("input");
			   inputs.each((index,input)=>{
				//將裡面每個input都設為required
				input.setAttribute("required","true");
			   });
	       } else {
	           detailsRow.css("display","none");
			   let inputs =detailsRow.find("input");
			   inputs.each((index, input) => {
	              input.removeAttribute("required"); // 移除 required
	          });
			  
	       }
	   } else {
	       console.error("無法找到對應的詳細行");
	   }
});



// 表單驗證
function validateForm(e) {
	e.preventDefault(); // 阻止預設的提交行為
    const form = document.getElementById('returnForm');
    const agreeTerms = document.getElementById('agreeTerms');
    let valid = true;

	
    // 檢查是否勾選「同意退貨條款」
    if (!agreeTerms.checked) {
        alert("請同意退貨條款！");
        valid = false;
    }

    // 檢查每個勾選的商品是否填寫了退貨信息
    const checkboxes = document.querySelectorAll("input.choose-item");
	console.log(checkboxes);
    checkboxes.forEach((checkbox) => {
        if (checkbox.checked) {
			
			let inputs =checkbox.closest("tbody").querySelectorAll("input");
			inputs.forEach((input)=>{
				input.setAttribute("required", "true");
			});
			
			if(inputs){
				let reason =checkbox.closest("tbody").querySelector("input.reason");
				let quantity =checkbox.closest("tbody").querySelector("input.quantity");
				let photo =checkbox.closest("tbody").querySelector("input.photo");
				
				if(reason.value.trim()==""||quantity.value.trim()==""||photo.value.trim()==""){
					alert("請完整填寫退貨資訊");
					valid = false;	
				}
				
				if(parseInt(quantity.value.trim())<=0){
					alert("退貨數量不可小於等於0");
					valid = false;	
					
				}
				
				
			}
								
			
 
        }
    });
		
	
	// 檢查是否至少勾選一個商品
	   let isAnyChecked = Array.from(checkboxes).some((checkbox) => checkbox.checked);
	   if (!isAnyChecked) {
	       alert("請至少勾選一個商品進行退貨！");
	       valid = false;
	   }
	
	

    if (valid) {
        let is_comfirm =confirm("請問要送出退貨申請嗎?");
		if(is_comfirm){
			alert("您已成功送出退貨申請!");
			form.submit();
		}
		
		
    }
}



$("input.photo").on("change",function(){
	console.log(this.files);
});
	
	
	
	
	
	