package com.cia103g5.user.order.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cia103g5.user.member.dto.SessionMemberDTO;
import com.cia103g5.user.order.model.OrdersService;
import com.cia103g5.user.order.model.OrdersVO;
import com.cia103g5.user.order.model.ReturnInfo;
import com.cia103g5.user.order.model.dto.OrderSummaryDTO;
import com.cia103g5.user.order.model.dto.ResponseDTO;
import com.cia103g5.user.order.model.dto.StatisticDTO;
import com.cia103g5.user.order.util.ApiResponse;
import com.cia103g5.user.orderDetails.model.OrderDetailService;
import com.cia103g5.user.orderDetails.model.OrderDetailVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Autowired
	OrdersService orderSvc;
	
	@Autowired
	OrderDetailService detailSvc;
		
	//列出所有訂單(用訂單明細關聯到訂單)->預覽只要有任一份商品資訊即可，詳情才有該訂單所有明細
	@GetMapping("/mem_order")
	public String toMemOrderPage(ModelMap model,HttpSession session) {
		SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer memberId = sessionMember.getMemberId();
		
		
		List<OrderSummaryDTO> list =orderSvc.getOrderSummaryByMemId(memberId);
		//之後請用mem_id查詢該會員所有的訂單
//		List<OrderSummaryDTO> list =orderSvc.getOrderSummary();
		model.addAttribute("OrderSumDTO",list);
		
		
				
		return "order/mem_order";
	}
	
	//點擊取消訂單，變更訂單狀態的API，回傳一個ApiResponse，包著訊息(Spring boot 自動將物件轉成JSON輸出，不需設contentType)
	@ResponseBody
	@PutMapping("/member/{orderId}/cancel")
	public ApiResponse cancelOrder(@PathVariable Integer orderId) {
		Boolean success;
		String message;
		if(orderSvc.alterOrderState(orderId,(byte)1)) {
			Integer cancel =orderSvc.alterShipStatus(orderId, (byte)3);
			System.out.println("取消"+cancel+"筆訂單");
			
			success=true;
			message="Order canceled successfully";
		}else {
			success=false;
			message="Order cancellation failed";
		}
		
		return new ApiResponse(success,message);
	}
	
	
	//點擊完成訂單，變更狀態orderState為3的API，回傳一個ApiResponse，包著成功訊息。
	@ResponseBody
	@PutMapping("/member/{orderId}/finished")
	public ApiResponse orderFinished(@PathVariable Integer orderId) {
		Boolean success;
		String message;
		
		if(orderSvc.alterOrderState(orderId,(byte)3)) {
			orderSvc.alterShipStatus(orderId, (byte)5);
			success=true;
			message="Order canceled successfully";
		}else {
			success=false;
			message="Order cancellation failed";
		}
			
		return new ApiResponse(success,message);
	}
	
	
	
	
	//傳入會員編號、訂單狀態，回傳DTO集合(已登入的情況)-->(要修改路徑、參數，memId從session取，不會從路徑取)
	@ResponseBody
	@GetMapping("/member/orderState/login/{orderState}")
	public List<OrderSummaryDTO> fetchOrdersByOrderStateByMemId(@PathVariable Byte orderState,HttpSession session){
		SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer memberId = sessionMember.getMemberId();
		
		return orderSvc.getOrderSummaryByOrderStateAndMemId(orderState,memberId);
		
	}
	
	//<<依照不同物流狀態的查詢API>>-->全部訂單
	@ResponseBody
	@GetMapping("/member/shipStatus/{shipStatus}")
	public List<OrderSummaryDTO> fetchOrdersByShipStatus(@PathVariable Byte shipStatus){
		return orderSvc.getOrderSummaryByShipStatus(shipStatus);
		
	}
	
	//<<依照不同物流狀態的查詢API>>-->會員登錄後，要從session取得memId
		@ResponseBody
		@GetMapping("/member/shipStatus/login/{shipStatus}")
		public List<OrderSummaryDTO> fetchOrdersByShipStatusByMemId(@PathVariable Byte shipStatus,HttpSession session){
			SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
			Integer memberId = sessionMember.getMemberId();
			
			return orderSvc.getOrderSummaryByShipStatusAndMemId(shipStatus,memberId);
			
		}
	
	
	
	//占卜師變更出貨狀態為 "已出貨"，返回原頁面
	@GetMapping("/ft_order/delivery/{OrderNo}")
	public String changeShipStatusOrderNo(@PathVariable Integer OrderNo,ModelMap model,HttpSession session) {
//		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
//		Integer ftId = sessionMember.getFtId();
		
		orderSvc.alterShipStatus(OrderNo, (byte)1);
		queryFtOrders(model,session);
		
		return "order/ft_order";
	}
	
	
	
	 //接收要轉交到占卜師訂單畫面的請求
	@GetMapping("/ft_order")
	public String queryFtOrders(ModelMap model,HttpSession session) {
		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
		Integer ftId = sessionMember.getFtId();
		
		List<OrdersVO> list = orderSvc.getAllByFtId(ftId);
				
//		List<OrdersVO> list =orderSvc.getAll();
		model.addAttribute("allOrders",list);
		
		//轉換時間
		Map<Integer,String[]> timemap = transferDate(list);
		model.addAttribute("timemap",timemap);
		
		//轉換訂單狀態
		Map <Integer,String> statemap =transferOrderState(list);
		model.addAttribute("statemap",statemap);
		
		//轉換付款方式
		Map <Integer,String> paymap =transferPay(list);
		model.addAttribute("paymap",paymap);
		
		//轉換物流狀態
		Map <Integer,String> shipmap =transferShipStatus(list);	
		model.addAttribute("shipmap",shipmap);
		
		return "order/ft_order";
	}
	
	//占卜師查看訂單明細(用orderNo 查)
	@ResponseBody
	@GetMapping("/ft_order/detail/{orderNo}")
	public ResponseEntity<Map<String,Object>> checkOrderDetail(@PathVariable String orderNo) {
		
		Integer orderno =null;
		 ResponseDTO<OrdersVO,OrderDetailVO> DTO =new ResponseDTO<OrdersVO,OrderDetailVO>();
		 if(orderNo.trim()!="" && orderNo.trim()!=null) {
			 orderno =Integer.valueOf(orderNo);
		 }		 
		 
		 OrdersVO ordersVO =orderSvc.getOneOrder(orderno);
		 List<OrderDetailVO> detailVOList =detailSvc.getAllDetailsByOrderNo(orderno);
		 
		 Map<String, Object> response = new HashMap<>();
		    response.put("order", ordersVO);
		    response.put("details", detailVOList);

		    // 返回 ResponseEntity 並設置 HTTP 狀態
		    return ResponseEntity.ok(response);
		
	}
	
	
	//轉交到占卜師訂單統計的畫面，傳誦可選年份、上個月、全部的統計
	@GetMapping("/ft_order/statistics")
	public String toFtStatisticsPage(ModelMap model,HttpSession session) {
		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
		Integer ftId = sessionMember.getFtId();
		
//		Integer ftId =1;
		Integer firstYearOfOrder =orderSvc.getFirstOrderYearFromFtId(ftId);
		
		System.out.println("first year of order:"+firstYearOfOrder);
		
		Integer  currentYear = LocalDate.now().getYear();//今年年份	
		List<Integer> allYears = new ArrayList<Integer>();
		
		for(int i =firstYearOfOrder;i<=currentYear;i++) {
			allYears.add(i);
		}
		
		model.addAttribute("yearList",allYears);
		
		StatisticDTO lastMonth =orderSvc.getLastMonthStatisticsByFt(ftId);
		model.addAttribute("lastMonth",lastMonth);
		
		StatisticDTO all =orderSvc.getAllStatisticByFtId(ftId);
		model.addAttribute("all", all);
		
		
		return "order/ft_statistic";
	}
	
	
	
	
	
	//轉交到訂單明細頁面(傳送點擊的訂單編號)
	@GetMapping("/mem_order/detail")
	public String toMemberOrderDetail(ModelMap model,@RequestParam("orderNo") String orderNo) {
		
		Integer orderno =Integer.valueOf(orderNo);
		OrdersVO ordersVO =orderSvc.getOneOrder(orderno);		
		//傳送該訂單的訂單資訊
		model.addAttribute("ordersVO",ordersVO);
		
		List<OrdersVO> list =new ArrayList<OrdersVO>();
		list.add(ordersVO);
		
		//轉換時間
		Map<Integer,String[]> timemap = transferDate(list);
		model.addAttribute("timemap",timemap);
		
		//轉換付款方式
		Map <Integer,String> paymap =transferPay(list);
		model.addAttribute("paymap",paymap);
		
		
		List<OrderDetailVO> detailVOList =detailSvc.getAllDetailsByOrderNo(orderno);
		//傳送該訂單的所有明細資訊(裝在List)
		model.addAttribute("detailLists",detailVOList);
				
		
		return "order/order_detail_mem";
	}
	
	
	
	//轉交到退貨申請頁面
	@PostMapping("/member/return")
	public String toReturnPage(ModelMap model,@RequestParam("orderNo") String orderNo) {
		//依訂單編號查到該訂單
		OrdersVO ordersVO =orderSvc.getOneOrder(Integer.valueOf(orderNo));
		model.addAttribute("ordersVO",ordersVO);
		
		List<OrderDetailVO> detailVOList =detailSvc.getAllDetailsByOrderNo(Integer.valueOf(orderNo));
		//傳送該訂單的所有明細資訊(裝在List)
		model.addAttribute("detailLists",detailVOList);
						
		return "order/apply_return";
	}
	
	
	//點選退貨-送出退貨，變更訂單狀態和物流狀態
	@PostMapping("/member/return/submit")
	public String getReturnForm(
			ModelMap model,
	        @RequestParam("orderNo") String orderNo,
	        @RequestParam Map<String, String> params,//取得所有型態string的參數的map，key:name value:參數值
	        @RequestParam Map<String, MultipartFile> files,
	        HttpSession session//取得所有型態為multipartFile的map，key:name value:檔案
	) throws IOException {
		SessionMemberDTO sessionMember = (SessionMemberDTO) session.getAttribute("loggedInMember");
		Integer memberId = sessionMember.getMemberId();
		
		//檢查是否更新成功，決定後續更新狀態
		Boolean check_success =false;
		
		//創建一個map，key:prodno、value:ReturnInfo
	    Map<Integer, ReturnInfo> returnInfoMap = new HashMap<>();
	    
	    //1.更新訂單明細的資料、上傳退貨資訊
	    // 從參數中依照其name，找出所有開頭為item[的參數，取得裡面藏的prodno字串
	    for (String key : params.keySet()) {
	        if (key.startsWith("items[")) {
	            String prodNo = key.substring(key.indexOf('[') + 1, key.indexOf(']'));
	            
	            ReturnInfo info;
	            if (returnInfoMap.containsKey(Integer.valueOf(prodNo))) {
	                info = returnInfoMap.get(Integer.valueOf(prodNo));//若該key存在，則取得該key的value(一個ReturnInfo物件)
	            } else {
	                info = new ReturnInfo();//若該key不存在(不同的prodno)，則創建一個新的物件，存回map
	                returnInfoMap.put(Integer.valueOf(prodNo), info);//map存入一個key-value(prodno,returnInfo)
	            }
	            
	        }
	    }

	    //取出每個存入map的key(prodno)，取得物件，手動設置各個資訊
	    for (Integer prodNo : returnInfoMap.keySet()) {
	    	//取得存入map的物件
	        ReturnInfo info = returnInfoMap.get(prodNo);
	        info.setApplyTime(LocalDate.now());//設置時間
	        info.setProdNo(prodNo);
	        
	        String reason =params.get("items["+String.valueOf(prodNo)+"].reason");
	        if (reason != null && !reason.trim().isEmpty()) {
	            info.setReason(reason); // 設置原因
	        }
	        
	        String quantityStr = params.get("items[" + String.valueOf(prodNo) + "].quantity");
	        if (quantityStr != null && !quantityStr.trim().isEmpty()) {
	            Integer quantity = Integer.valueOf(quantityStr);
	            info.setQuantity(quantity); // 設置數量
	        }
	        
	        MultipartFile file = files.get("files["+String.valueOf(prodNo)+"]");//取得照片
	        if (file != null && !file.isEmpty()) {
	            byte[] picture = file.getBytes();

	            //轉換成JSON
	            ObjectMapper objectMapper = new ObjectMapper();
	            objectMapper.registerModule(new JavaTimeModule());//java 8的LoaclDate需要註冊才支援被jackson轉成JSON
	            
	            String json =objectMapper.writeValueAsString(info);
	            
	            if(detailSvc.updateReturnInfo((byte) 1, json, picture, Integer.valueOf(orderNo), prodNo)) {
	            	check_success=true;
	            }
	            
	            
	        }
	    }
	    
	    if(check_success) {
	    	 //2.更新訂單狀態:申請退貨(orderState=2)
		    orderSvc.alterOrderState(Integer.valueOf(orderNo),(byte)2);
		    
		    //3.更新物流狀態:已到貨(shipStatus=4)
		    orderSvc.alterShipStatus(Integer.valueOf(orderNo), (byte)4);
	    }
	   	    	
		List<OrderSummaryDTO> list =orderSvc.getOrderSummaryByMemId(memberId);
		model.addAttribute("OrderSumDTO",list);
		
			
		return "order/mem_order";
	}


	
	
	@GetMapping("/")
	public String toIndex() {
		return "index";
	}
	
	@GetMapping("/processing")
	public String tab_processing() {
		return "order/processing";
	}
	
	@GetMapping("/delivery")
	public String tab_delivery() {
		return "order/delivery";
	}
	
	@GetMapping("/finished")
	public String tab_finished() {
		return "order/finished";
	}
	
	@GetMapping("/return_product")
	public String tab_return_prod() {
		return "order/return_product";
	}
	
	
	
//	@GetMapping("/admin/order_statistics")
//	public String to_order_statistics() {
//		return "admin/order_statistics";
//	}
	
//	@GetMapping("/admin/return_order")
//	public String return_order_page() {
//		return "admin/return_order";
//	}
	
	

	
//時間轉換的方法	
public Map<Integer,LocalDate> transferTime (List<OrdersVO> list){
		
		Map<Integer,LocalDate> map = new HashMap<>(); 
		
		for(OrdersVO ordersVO : list) {
			if(ordersVO.getCreatedTime()!=null) {
				LocalDate date = ordersVO.getCreatedTime().toLocalDateTime().toLocalDate();
				map.put(ordersVO.getOrderNo(), date); //每個ordersVO的order_no作為key，轉換後的日期作為value，加入map當中
				
			}else {
				System.out.println("prod_no " + ordersVO.getOrderNo() + "的listed_time為null");
			}
	
		}
		
		return map;
	}

//自訂方法:日期轉換 //key:訂單編號，value:[0]成立時間、[1]結案時間
public Map<Integer,String[]> transferDate(List<OrdersVO>list){
	Map<Integer,String[]> map = new HashMap<>(); 
	
	for(OrdersVO order:list) { //第一個索引存訂單成立
		String [] dateArr =new String[2];
		
		if(order.getCreatedTime()!=null) {
			dateArr[0] =order.getCreatedTime().toLocalDateTime().toLocalDate().toString();			
		}else {
			dateArr[0]="-";
		}
		
		if(order.getEndedTime()!=null) {//第二個索引存訂單結案
			dateArr[1] =order.getEndedTime().toLocalDateTime().toLocalDate().toString();		
		}else {
			dateArr[1]="-";
		}
		map.put(order.getOrderNo(), dateArr);//key:訂單編號,value:成立時間、結案時間(字串)
	}
	
	return map;
}


//自訂方法:轉換付款方式
public Map <Integer,String> transferPay(List<OrdersVO> list){
	
	Map <Integer,String> map =new HashMap<>(); 
	for(OrdersVO ordersVO : list) {
		if(ordersVO.getPayment()!=null) {
			String paymethod="未知付款方式";
			switch(ordersVO.getPayment()) {
			case 0:
				paymethod="信用卡";
				break;
			case 1:
				paymethod="ATM轉帳";
				break;
			case 2 :
				paymethod ="超商繳費";
				break;
			case 3:
				paymethod ="貨到付款";
			}	
			map.put(ordersVO.getOrderNo(),paymethod);
		}
	}
	
	return map;
}

//自訂方法:轉換物流狀態
public Map <Integer,String> transferShipStatus(List<OrdersVO> list){
	
	Map <Integer,String> map =new HashMap<>(); 
	for(OrdersVO ordersVO : list) {
		if(ordersVO.getShipStatus()!=null) {
			String status="未知狀態";
			switch(ordersVO.getShipStatus()) {
			case 0:
				status="待出貨";
				break;
			case 1:
				status="已出貨";
				break;
			case 2 :
				status ="已送達";
				break;
			case 3 :
				status="待出貨"; //待出貨但被取消
				break;
			case 4 :
				status="已送達";//已送達但被申請退貨
				break;
			case 5:
				status="已送達"; //已送達且訂單完成
			}	
			map.put(ordersVO.getOrderNo(),status);
		}
	}
	
	return map;
}

	
//自訂方法:轉換訂單狀態

public Map <Integer,String> transferOrderState(List<OrdersVO> list){
	
	Map <Integer,String> map =new HashMap<>(); 
	for(OrdersVO ordersVO : list) {
		if(ordersVO.getOrderState()!=null) {
			String status="未知狀態";
			switch(ordersVO.getOrderState()) {
			case 0:
				status="已成立";
				break;
			case 1:
				status="已取消";
				break;
			case 2 :
				status ="退貨";
				break;
			case 3 :
				status= "已完成";			
			}	
			map.put(ordersVO.getOrderNo(),status);
		}
	}
	
	return map;
}




	
}
