//發生input="file"發生change事件時，讀取上傳的檔案，轉換格式，取代為新的src
let file = document.querySelector("input.product-img");
let preview_pic =document.createElement("img");
let preview_block =document.querySelector("div.drop_zone");

file.addEventListener("change",function(){
	
	if(file.files!=null){
		for(i=0;i<this.files.length;i++){
			let reader = new FileReader();
			reader.readAsDataURL(this.files[i]); 
							
			reader.addEventListener("load",function(){				
				preview_block.innerHTML="";
				preview_pic.src = reader.result;
				preview_block.appendChild(preview_pic);					

			});
		}
				
		let next_pic =document.querySelector("div.two-block");
		next_pic.classList.remove("hidden");
					
	}
});

let filecopy = document.querySelector("input.img-two");
let block =document.querySelector("div.two");

filecopy.addEventListener("change",function(){
		
	if(filecopy.files!=null){
		for(i=0;i<this.files.length;i++){
			let reader = new FileReader();
			reader.readAsDataURL(this.files[i]); 
							
			reader.addEventListener("load",function(){
				block.innerHTML="";				
				let pic =document.createElement("img");														
				pic.src = reader.result;
												
				block.appendChild(pic);				

			});
		}
		
			
		let next_pic =document.querySelector("div.three-block");
		next_pic.classList.remove("hidden");
					
	}
});

let filethree = document.querySelector("input.img-three");
let picthree =document.createElement("img");
let blockthree =document.querySelector("div.three");

filethree.addEventListener("change",function(){
	
	if(filethree.files!=null){
		for(i=0;i<this.files.length;i++){
			let reader = new FileReader();
			reader.readAsDataURL(this.files[i]); 
							
			reader.addEventListener("load",function(){				
				blockthree.innerHTML="";
				picthree.src = reader.result;
				blockthree.appendChild(picthree);					

			});
		}
				
		
					
	}
});






