package com.cia103g5.user.product.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.member.dto.SessionMemberDTO;
import com.cia103g5.user.product.model.ProductService;
import com.cia103g5.user.product.model.ProductVO;
import com.cia103g5.user.productImage.model.ProductImageService;
import com.cia103g5.user.productImage.model.ProductImageVO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/product") //為基礎url-pattern，只要源自/product開頭的請求都會到此處理
public class ProductController {

	@Autowired
	ProductService prodSvc;	
	
	@Autowired
	ProductImageService prodimgSvc;
	
	//頁面跳轉
	//回首頁
	@GetMapping("/")
	public String toIndex() {
		return "index";
	}
	
	
//	@GetMapping("/product_list")
//	public String toAllProdPage() {
//		return "product/product_list";
//	}
		
	
	@GetMapping("product_statistics")
	public String toStatisticsPage(ModelMap model,HttpSession session) {
		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
		Integer ftId = sessionMember.getFtId();
		
		List<ProductVO> list =prodSvc.getFtAllProductExcludeDelete(ftId); 
		model.addAttribute("average",average(list));
		model.addAttribute("allProductList",list);	
		
		return "product/product_statistics";
	}
	
	@GetMapping("add_product") //完整的url-pattern相當於/product/add_product
	public String addProdPage(ModelMap model) {
		ProductVO productVO =new ProductVO();
		model.addAttribute("productVO",productVO);
		
		return "product/add_product";
	}
	
	
//	@GetMapping("/update_page") //為自訂的url-mapping，html的href需對應到此
//	public String updateProdPage() {
//		return "product/update_page"; //前者為資料夾、後者為檔案名稱
//	}
	
		
	@PostMapping("insert") 
	public String insert(@ModelAttribute("productVO")ProductVO productVO,@RequestParam("photo")MultipartFile[] parts ,ModelMap model, HttpSession session) throws IOException {
		//先新增商品現有資訊(不包含照片)
		//先自訂，之後取出session存入的ft_id
		
		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
		Integer ftId = sessionMember.getFtId();
		
		//預設的ftId(1號)
		FtVO ftVO = new FtVO();
		ftVO.setFtId(ftId);
		
		productVO.setFtId(ftVO);
		productVO.setListedTime(new java.sql.Timestamp(System.currentTimeMillis()));
		prodSvc.addProduct(productVO);
		
		
		//迴圈取出每個part，依序存入productImageVO	
		for(int i=0;i<parts.length;i++) {
			if(!parts[i].isEmpty()) {				
				ProductImageVO imgVO =new ProductImageVO();		
				imgVO.setProdPic(parts[i].getBytes());
				imgVO.setCreatedTime(new java.sql.Timestamp(System.currentTimeMillis()));
				imgVO.setProductVO(productVO);
				if(i==0) {//第一張圖值皆設為主要圖片
					imgVO.setIsPrimary((byte)1);
				}else {
					imgVO.setIsPrimary((byte)0);
				}
				prodimgSvc.addProductImg(imgVO);
			}
		}
								
		//取得新增後的所有商品，返回到所有商品列表頁面
		getAll(model,session);
		return "product/product_list";
	}
	
	//<<查詢全部商品>>
	@GetMapping("queryAllProduct")
	//之後要在參數內放入@sessionAttribute，取出存在session當中的ft_id
	public String getAll(ModelMap model,HttpSession session) {
		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
		Integer ftId = sessionMember.getFtId();
		
//		尚未取得ft_id，先不指定占卜師會員。取得ft_id後，使用以下方法，取得單一占卜師的所有商品
		List<ProductVO> list =prodSvc.getFtAllProductExcludeDelete(ftId); 
//		List<ProductVO> list =prodSvc.getAllExcludeDelete();
		
		//轉換所有商品時間、單獨存入model中(map的形式)
		Map<Integer,LocalDate> timemap =transferTime(list);
		model.addAttribute("timemap",timemap);
		
		model.addAttribute("allProductList",list);		
		return "product/product_list";
	}
	
	//<<輸出商品圖片-servletOutputStream>>
	@GetMapping("showPic")
	public void writePic(@RequestParam("prodno") String prodno,HttpServletResponse res ) throws IOException {
		
		System.out.println("prodno : "+prodno);
		//輸出該商品is_primary=1，設為主要照片的圖片
		ProductImageVO product_imageVO =prodimgSvc.findPrimaryImageByProdNo(Integer.valueOf(prodno));
		ServletOutputStream out = res.getOutputStream();		
		
		if(product_imageVO!=null &&product_imageVO.getProdPic()!=null) {
			
			byte[] picdata =product_imageVO.getProdPic();
			res.setContentType("image/jpeg");
			
			out.write(picdata);
		}else {
			Resource resource =new ClassPathResource("static/img/default.jpg");
			InputStream fs =resource.getInputStream();
			BufferedInputStream bis =new BufferedInputStream(fs);
			
			//讀取預設的照片
			byte[] defaultpic =bis.readAllBytes();
			out.write(defaultpic);
					
			}
			
			out.close();		
			
	}
	
	
	//<<功能:修改單一產品資訊-64base輸出單一商品預存照片>>
	//取得傳入的產品編號的請求參數，跳轉到更新畫面，並且查詢單一商品，取出作為預設資料
	//<<功能:更新>>
	@PostMapping("update_product")
	public String updateProduct(@RequestParam("prodno") String prodno,ModelMap model) throws IOException {
		//取得產品編號，轉換為integer
		Integer oneProd =Integer.valueOf(prodno);
		ProductVO product =prodSvc.getOneProduct(oneProd);
		
		Byte status =product.getStatus();
		model.addAttribute("status",status);
		
		//查詢後存入該商品的資訊
		model.addAttribute("product",product);//相當於request.setAttribute
		
		//查詢單一商品照片，用base64輸入
		//查詢主要圖片		
		ProductImageVO productImageVO =prodimgSvc.findPrimaryImageByProdNo(Integer.valueOf(prodno));
		
		String picString;
		
		if(productImageVO!=null &&productImageVO.getProdPic()!=null) {
			byte[] picdata =productImageVO.getProdPic();
			
					picString = Base64.getEncoder().encodeToString(picdata);					
					
					model.addAttribute("base64string",picString);
			}else {
					Resource resource =new ClassPathResource("static/img/default.jpg");
					InputStream fs =resource.getInputStream();
					BufferedInputStream bis =new BufferedInputStream(fs);
					
					//讀取預設的照片
					byte[] defaultpic =bis.readAllBytes();
					
					picString =Base64.getEncoder().encodeToString(defaultpic);
					System.out.println("輸出預設圖片");
					
					model.addAttribute("base64string",picString);
					
					bis.close();
					fs.close();
				}
		//查詢其他圖片，存入map(imageNo、的圖片)e64圖片)
		Map<Integer,String> imgMap =new HashMap<Integer,String>();
		
		List<ProductImageVO> list =prodimgSvc.getOtherPicVO(oneProd);
		for(int i=0;i<list.size();i++) {
			if(list.get(i)!=null && list.get(i).getProdPic()!=null) {
				String base64 =Base64.getEncoder().encodeToString(list.get(i).getProdPic());
				Integer imageNo =list.get(i).getImageNo();
				System.out.println("imageNo : " + imageNo);
				imgMap.put(imageNo, base64);
			}
					
		}
		
		model.addAttribute("otherpics",imgMap);
		
									
		return "product/update_page";
	}
	
	//送出更新商品的資料
	@PostMapping("send_update_product")
	public String sendUpdateProduct(@ModelAttribute("productVO")ProductVO productVO,
									@RequestParam("photo")MultipartFile part ,//取得主要圖片
									@RequestParam Map<String, String> allParams,//取得所有參數
									@RequestParam("photos") MultipartFile[] otherPhotos,//取得其他圖片檔案
									@RequestParam("status")String status,ModelMap model,HttpSession session) throws IOException {
				//先更新商品現有資訊(不包含照片)
				//依照prod_no(PK)來更新商品資訊，商品原本的ft_id為多少，就直接存入
				if("上架".equals(status)) {
					productVO.setStatus((byte)1);
				}else if("下架".equals(status)) {
					productVO.setStatus((byte)0);
				}
				productVO.setListedTime(new java.sql.Timestamp(System.currentTimeMillis()));
				
				prodSvc.updateProduct(productVO);
							
						
				//兩種情況:(1)有傳照片-找到商品圖片更新(2)沒傳照片-查出原圖片-更新	
				//處理主照片(從prodno找到)		
				ProductImageVO imgVO =prodimgSvc.findPrimaryImageByProdNo(productVO.getProdNo());
				byte[] buf;
				if(imgVO!=null) {//如果原本有圖片
					if(!part.isEmpty()) {//有上傳
						buf =part.getBytes(); //取得上傳圖片更新，反之使用原本圖片更新
					}else {
						buf=imgVO.getProdPic();
					
					}
					imgVO.setProdPic(buf);
					imgVO.setIsPrimary((byte)1);
					
					System.out.println("更新圖片 ID：" + imgVO.getImageNo());
					prodimgSvc.updateProductImg(imgVO);
				}else {
					if(!part.isEmpty()) {//如果原本沒有圖片
						buf =part.getBytes(); //則取得上傳圖片更新(主圖為必填)
						ProductImageVO newImgVO = new ProductImageVO();
						newImgVO.setIsPrimary((byte)1);
						newImgVO.setProductVO(productVO);
						newImgVO.setProdPic(buf);
						prodimgSvc.addProductImg(newImgVO);
						
						System.out.println("新增圖片 ID：" + newImgVO.getImageNo());
					}
					
				}
				
				System.out.println("接收到的主要圖片大小：" + (part.isEmpty() ? "無圖片" : part.getSize()));
				System.out.println("接收到的其他圖片數量：" + otherPhotos.length);

				
				//存放其他圖片的編號、檔案
				 Map<Integer, MultipartFile> photoMap = new HashMap<>();
				 
				 int fileIndex = 0; // 
				 //for-each所有來自於http請求的參數
				 for(Map.Entry<String, String> entry : allParams.entrySet()) {			 
					 if(entry.getKey().startsWith("imageNo_")) {//篩出name="imageNO"開頭的參數
						 if(entry.getValue()!=null && !entry.getValue().trim().isEmpty()) {
							 Integer imageNo = Integer.parseInt(entry.getValue()); //取得圖片編號
							 //找到一個圖片參數後，從multipartFile[]當中索引值0開始匹配
							 if (fileIndex < otherPhotos.length ) {				                
					             System.out.println("imageNo:"+imageNo);  
					             System.out.println("匹配的part index:"+fileIndex);
								 photoMap.put(imageNo, otherPhotos[fileIndex]);
					                fileIndex++; 
					            }	 
						 }
						 		 			 
					 }				
				 }
				 
//			從分類好的photoMap再取出每個map，依照其是否有上傳檔案，決定作更新或查回原檔更新的動作
				 for(Map.Entry<Integer, MultipartFile> picture : photoMap.entrySet()) {
					 byte[] data;
					 Integer imgNo =picture.getKey();
					 ProductImageVO otherImgVO =prodimgSvc.getOneProductImg(imgNo);
					 
					 if(!picture.getValue().isEmpty()) {					 
						  data=picture.getValue().getBytes();
					 }else {
						 data =otherImgVO.getProdPic();		
					 }
					 otherImgVO.setProdPic(data);
					 otherImgVO.setIsPrimary((byte)0);
					 prodimgSvc.updateProductImg(otherImgVO);					 
				 }
				 
												
				//取得更新後的所有商品，返回到所有商品列表頁面
				 SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
				 Integer ftId = sessionMember.getFtId();
				 
				 List<ProductVO> list =prodSvc.getFtAllProductExcludeDelete(ftId); 	
				Map<Integer,LocalDate> timemap =transferTime(list);
				model.addAttribute("timemap",timemap);			
				model.addAttribute("allProductList",list);	
		
		return "product/product_list";
	}
	
	//<<刪除:使用update更新status狀態，回傳product/product_list頁面，查詢全部的地方自訂排除status=2的商品>>	
	@PostMapping("delete_product")
	public String delete(@RequestParam("prodno") String prodno,ModelMap model,HttpSession session) {
		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
		Integer ftId = sessionMember.getFtId();	
		
		//依照點擊的prodno，查到該商品物件，變更狀態再update
		prodSvc.updateStatus(Integer.valueOf(prodno));
		
		//點即刪除更新狀態完為2，返回商品列表
		List<ProductVO> list =prodSvc.getFtAllProductExcludeDelete(ftId); 
		Map<Integer,LocalDate> timemap =transferTime(list);
		model.addAttribute("timemap",timemap);			
		model.addAttribute("allProductList",list);		
		
		return "product/product_list";
	}
	
	
	
	
	//自訂方法:將productVO 集合每一個timestamp時間取出，分別存入一個map(key="prodno",value="time")，回傳一個Map<Integer,LocalDate>
	public Map<Integer,LocalDate> transferTime (List<ProductVO> list){
		
		Map<Integer,LocalDate> map = new HashMap<>(); 
		
		for(ProductVO product : list) {
			if(product.getListedTime()!=null) {
				LocalDate date = product.getListedTime().toLocalDateTime().toLocalDate();
				map.put(product.getProdNo(), date); //每個productVO的prodno作為key，轉換後的日期作為value，加入map當中
				
			}else {
				System.out.println("prod_no " + product.getProdNo() + "的listed_time為null");
			}
	
		}
		
		return map;
	}
	
	
	//自訂方法:計算商品平均評分
	public Map<Integer,Integer> average(List<ProductVO> list){
		
		Map<Integer,Integer> map = new HashMap<>(); 
		for(ProductVO product : list) {
			if(product.getRatingCount()!=null && product.getRatingCount()>0 && product.getRatingCount()!=null) {
				Integer average =Math.round((float)product.getRating()/product.getRatingCount());
				map.put(product.getProdNo(), average);
			}else {
				map.put(product.getProdNo(), 0);
			}
			
		}
		
		
		return map;
	}
	

}//-->class ends
