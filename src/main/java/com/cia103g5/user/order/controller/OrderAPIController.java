package com.cia103g5.user.order.controller;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cia103g5.user.member.dto.SessionMemberDTO;
import com.cia103g5.user.order.model.OrdersService;
import com.cia103g5.user.order.model.dto.AdminOrderTransferDTO;
import com.cia103g5.user.order.model.dto.CommentModalDTO;
import com.cia103g5.user.order.model.dto.OrderQureyDTO;
import com.cia103g5.user.order.model.dto.OrderSummaryDTO;
import com.cia103g5.user.order.model.dto.ProductCommetDTO;
import com.cia103g5.user.order.model.dto.ScoringDTO;
import com.cia103g5.user.order.model.dto.StatisticDTO;
import com.cia103g5.user.order.model.dto.TransferOrderDTO;
import com.cia103g5.user.order.util.TimeTransfer;
import com.cia103g5.user.orderDetails.model.OrderDetailService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/order")
public class OrderAPIController {

	@Autowired
	OrdersService orderSvc;
	
	@Autowired
	OrderDetailService detailSvc;
	
	@Autowired
	TimeTransfer timeTransfer;
	//占卜師複合查詢訂單
	@PostMapping("/query/composite")
	public ResponseEntity<?> queryByComposite(@RequestBody OrderQureyDTO query){
		
		Timestamp startDate=null;
		Timestamp endDate=null;
		Integer memId =null;
		Integer orderNo =null;
		Byte orderState =null;
		Byte shipStatus =null;
		
		if(query.getStartDate()!=null && query.getEndDate()!=null) {
			startDate =timeTransfer.getStartOfDay(query.getStartDate().trim());
			endDate = timeTransfer.getEndOfDay(query.getEndDate().trim());
		}
		
		if(query.getMemId()!=null && query.getMemId()!="") {
			memId =Integer.valueOf(query.getMemId().trim());
		}
		
		if(query.getOrderNo()!=null && query.getOrderNo()!="") {
			orderNo =Integer.valueOf(query.getOrderNo().trim());
		}
		
		if(query.getOrderState()!=null && query.getOrderState()!=""){
			orderState =Byte.valueOf(query.getOrderState());
		}
		
		if(query.getShipStatus()!=null && query.getShipStatus()!=""){
			shipStatus =Byte.valueOf(query.getShipStatus());
		}
		
		List<TransferOrderDTO> list =orderSvc.getOrdersByCompositeQuery(startDate, endDate, memId, orderNo, orderState, shipStatus);
		
		 return ResponseEntity.ok(Map.of(
			        "data", list, // 返回查詢結果
			        "isEmpty", list.isEmpty()// 標註是否為空集合
			    ));	
		
	}
	
	
	//管理員複合查詢訂單的API
	@PostMapping("/query/composite/byAdmin")
	public ResponseEntity<?> queryByCompositeFromAdmin(@RequestParam("orderNo")String orderNo,
														@RequestParam("memId")String memId,
														@RequestParam("ftId")String ftId){
		Integer ordernumber=null;
		Integer memberid =null;
		Integer ftid =null;
		
		if(!orderNo.isEmpty()&& orderNo!=null) {
			ordernumber =Integer.valueOf(orderNo);
		}
		
		if(!memId.isEmpty()&& memId!=null) {
			memberid =Integer.valueOf(memId);
		}
		
		if(!ftId.isEmpty()&& ftId!=null) {
			ftid =Integer.valueOf(ftId);
		}
		
		List<AdminOrderTransferDTO> DTOs =orderSvc.compositeQueryOrderByAdmin(ordernumber, memberid, ftid);
	
		 return ResponseEntity.ok(Map.of(
			        "data", DTOs // 返回查詢結果(將物件存在key為data的value裡，需先用data作為key取出，接著才可繼續使用理面的資料)
			    ));	
	
	}
	
	//管理員查訂單統計
	@PostMapping("/query/chooseDate/byAdmin")
	public ResponseEntity<?> statisticOrdersChooseByYearAndMonth(@RequestParam("year")String year,@RequestParam("month")String month){
		
		Integer chooseYear =null;
		Integer chooseMonth =null;
		
		if(year.trim()!="" && year.trim()!=null) {
			chooseYear =Integer.valueOf(year);
		}
		
		if(month.trim()!="" && month.trim()!=null) {
			chooseMonth =Integer.valueOf(month);
		}
		
		StatisticDTO DTO =orderSvc.getOrderChooseByYearAndMonth(chooseYear, chooseMonth);
		
		return ResponseEntity.ok(DTO); //直接回傳整個物件，直接使用
	}
	
	//占卜師查詢訂單統計
	@PostMapping("/query/chooseDate/byFt")
	public ResponseEntity<?> statisticOrdersChooseByFt(@RequestParam("year")String year,@RequestParam("month")String month,HttpSession session){
		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
		Integer ftId = sessionMember.getFtId();	
		
//		Integer ftId =1;
		Integer chooseYear =null;
		Integer chooseMonth =null;
		
		if(year.trim()!="" && year.trim()!=null) {
			chooseYear =Integer.valueOf(year);
		}
		
		if(month.trim()!="" && month.trim()!=null) {
			chooseMonth =Integer.valueOf(month);
		}
		
		StatisticDTO DTO =orderSvc.orderStatisticsByChooseFromFt(chooseYear, chooseMonth, ftId);
		
		return ResponseEntity.ok(DTO); //直接回傳整個物件，直接使用
	}
	
	
	
	//<<不同訂單狀態的查詢API>>-->全部訂單
		@ResponseBody
		@GetMapping("/member/diffState/{orderState}")
		public List<OrderSummaryDTO> fetchOrdersByOrderState(@PathVariable Byte orderState){		
			return orderSvc.getOrderSummaryByOrderState(orderState);
			
		}
		
		
		//點擊評價燈箱，取得該訂單的訂單明細資訊，放到燈箱上(顯示商品細項，可個別作評論)
		@PostMapping("/mem_order/modalInfo")
		public ResponseEntity<List<CommentModalDTO>> showModalInfo(@RequestParam("orderNo") String orderNo){
			
			List<CommentModalDTO> commentList = detailSvc.getAllCommentModalInfo(Integer.valueOf(orderNo));
			return ResponseEntity.ok(commentList);
		}
		
	
		//評價商品的API，回傳成功的訊息
		@ResponseBody
		@PostMapping("/mem_order/addComment")
		public ResponseEntity<?> addProductComment(@RequestBody List<ProductCommetDTO> comment) {
			
			for(ProductCommetDTO DTO :comment) {
				System.out.println(DTO);
			}
			
//			 List<ScoringDTO> list = new ArrayList<ScoringDTO>();	
			 Boolean checkUpdate =detailSvc.addComment(comment);
			 System.out.println(checkUpdate);
			 
			if(checkUpdate){					
//				list =detailSvc.getProductCommentByOrderNo(Integer.valueOf(comment.get(0).getOrderNo()));
				return ResponseEntity.ok(Map.of("message", "Adding new comments for one order successfully!"));
			}else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Failed to add comments."));//若有問題返回錯誤響應
			}			
			
		}	
		
		//查看評價的API
		@PostMapping("/mem_order/viewComment")
		public ResponseEntity<List<ScoringDTO>> viewComment(@RequestParam("orderNo") String orderNo){
			Integer orderno =null;
			if(orderNo.trim()!=null && orderNo.trim()!="") {
				orderno = Integer.valueOf(orderNo);
			}
			
			List<ScoringDTO> list =detailSvc.getProductCommentByOrderNo(orderno);
			
			return ResponseEntity.ok(list);
		}
	
}
