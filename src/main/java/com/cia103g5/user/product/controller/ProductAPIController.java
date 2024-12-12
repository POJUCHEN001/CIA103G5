package com.cia103g5.user.product.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cia103g5.user.member.dto.SessionMemberDTO;
import com.cia103g5.user.product.model.ProductQueryDTO;
import com.cia103g5.user.product.model.ProductService;
import com.cia103g5.user.product.model.ProductVO;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/product")
public class ProductAPIController {

	@Autowired
    private ProductService productService;
	
	@Autowired
	private ProductController prodcontroller;
	
	//根據狀態查的API
		@PostMapping("/query/status")
		public ResponseEntity<?> queryByStatus(@RequestParam("status")String status,HttpSession session){
//			SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
//			Integer ftId = sessionMember.getFtId();
						
			if(status.length()==0 || status=="") {
				  return ResponseEntity.badRequest().body(Map.of(
				            "message", "Status parameter is required and cannot be empty.",
				            "status", 400
				        ));
			}
			
			//前端先抓住選擇狀態，將文字轉成對應數字傳入
			List<ProductVO> list = productService.getAllProductByStatusAndFtId(Byte.valueOf(status),1);			
			 Map<Integer,LocalDate>  timemap =prodcontroller.transferTime(list);
			
			  // 返回空集合時，明確告知前端
		    return ResponseEntity.ok(Map.of(
		        "data", list, // 返回查詢結果
		        "isEmpty", list.isEmpty(),// 標註是否為空集合
		        "time",timemap //每個productVO的建立時間
		    ));
					
		}
		
		//複合查詢的API
		@PostMapping("/query/composite")
		public ResponseEntity<?> queryByComposite(@RequestBody ProductQueryDTO query,HttpSession session){ 
//			SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
//			Integer ftId = sessionMember.getFtId();
			
			
			//預設傳入空字串將值設為null，反之則取得其傳入的值->空字串不可轉型成其他型態!
			Byte status=null;
			String prodName=null;
			Integer prodNo=null;
			
			if(query.getStatus()!=null && query.getStatus()!="") {
				status=Byte.valueOf(query.getStatus());
			}
			
			if(query.getProdName()!=null && query.getProdName()!="") {
				prodName=String.valueOf(query.getProdName());
			}
			
			if(query.getProdNo()!=null && query.getProdNo()!="") {
				prodNo=Integer.valueOf(query.getProdNo());
			}
			
			
			List<ProductVO> list =productService.findProductsByMutiConditionByFtId(status, prodName, prodNo, 1);				
					
			Map<Integer,LocalDate>  timemap =prodcontroller.transferTime(list);
			
			  return ResponseEntity.ok(Map.of(
				        "data", list, // 返回查詢結果
				        "isEmpty", list.isEmpty(),// 標註是否為空集合
				        "time",timemap //每個productVO的建立時間
				    ));
		}
		
		
	
}
