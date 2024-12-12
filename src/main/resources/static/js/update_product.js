//update頁面的預覽圖
let f = document.querySelectorAll("input.update-preview");

f.forEach(function(input){
	input.addEventListener("change",function(e){
		
		if(this.files!=null){
			for(let i=0;i<this.files.length;i++){
				let reader = new FileReader();
				reader.readAsDataURL(this.files[i]); 
					
				let currentInput = this;
							
				reader.addEventListener("load",function(){
					let block =currentInput.closest("div").querySelector("div.update-block");
					block.innerHTML="";		
					let pic =document.createElement("img");							
					pic.src = reader.result;
					block.appendChild(pic);
					
				});
			}						
		}
		
	});

});



