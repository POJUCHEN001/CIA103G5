package com.cia103g5.user.order.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cia103g5.user.order.model.OrdersService;
import com.cia103g5.user.order.model.ReturnInfo;
import com.cia103g5.user.order.model.dto.AdminOrderTransferDTO;
import com.cia103g5.user.order.model.dto.ShowReturnOrderDTO;
import com.cia103g5.user.order.model.dto.StatisticDTO;
import com.cia103g5.user.orderDetails.model.OrderDetailService;

@Controller
@RequestMapping("/order/admin")
public class AdminOrderController {

	@Autowired
	OrdersService orderSvc;
	
	@Autowired
	OrderDetailService detailSvc;
		
	@GetMapping("/statistic")
	public String toStatisticPage(ModelMap model) {
		
		Integer firstYearOfOrder =orderSvc.getFirstOrderYear();//第一筆訂單的年份
		Integer  currentYear = LocalDate.now().getYear();//今年年份
		
		List<Integer> allYears = new ArrayList<Integer>();
		for(int i =firstYearOfOrder;i<=currentYear;i++) {
			allYears.add(i);
		}
				
		model.addAttribute("yearList",allYears);
		
		StatisticDTO DTO =orderSvc.getLastMonthStatistics();
		model.addAttribute("DTO",DTO);
		
		
		return "admin/order_statistics";
	}
	
	@GetMapping("/statistic/detail/{orderNo}")
	public String toReturnDetailPage(ModelMap model,@PathVariable("orderNo")Integer orderNo) {
		
		//裝著這個訂單中所有有退的商品編號
		List<Integer> returnProdNo =detailSvc.getOneOrderAllReturnProdNo(orderNo);
		model.addAttribute("prodNoList",returnProdNo);
		
//		for(Integer prodno :returnProdNo) {
//			System.out.println("訂單編號: "+orderNo+"所有有退的商品編號:"+prodno);	
//		}
		
		Map<Integer,ReturnInfo> infoMap =detailSvc.getOneOrderAllReturnInfo(orderNo); 
		model.addAttribute("infoMap",infoMap);//infoMap的每個物件的key是prodno
		
//		for (Map.Entry<Integer, ReturnInfo> entry : infoMap.entrySet()) {
//		    System.out.println("infoMap Key: " + entry.getKey() + ", Value: " + entry.getValue());
//		    System.out.println("returnInfo:商品名字->"+entry.getValue().getName());
//		    System.out.println("returnInfo:退貨原因->"+entry.getValue().getReason());
//		    System.out.println("returnInfo:退款金額->"+entry.getValue().getPrice());
//		    System.out.println("returnInfo:申請時間->"+entry.getValue().getApplyTime());
//		    System.out.println("returnInfo:商品編號->"+entry.getValue().getProdNo());
//		    
//		}
		Integer totalReturnAmount =0;
		
		for(Map.Entry<Integer,ReturnInfo> entry : infoMap.entrySet()) {
			Integer productPrice =entry.getValue().getPrice();
			Integer returnAquantity =entry.getValue().getQuantity();
			totalReturnAmount+=productPrice*returnAquantity;
		}
		
		model.addAttribute("total",totalReturnAmount);
		
		
		
		Map<Integer,String> photoMap =detailSvc.getOneOrderAllReturnPic(orderNo);
		model.addAttribute("photoMap",photoMap);//photoMap的每個base64的key是prodno
		
//		for (Map.Entry<Integer, String> entry : photoMap.entrySet()) {
//		    System.out.println("photoMap Key: " + entry.getKey() + ", Value: " + entry.getValue());
//		}
	
		
		
		return "order/return_detail";
	}
	
	
	@GetMapping("/return_order")
	public String return_order_page(ModelMap model) {
		
		List<ShowReturnOrderDTO> dtos = detailSvc.getAllReturnOrder();
		model.addAttribute("dtos",dtos);
		
		return "admin/return_order";
	}
	
	@GetMapping("/query_order")
	public String admin_order_query(ModelMap model) {
		 List<AdminOrderTransferDTO> lists =orderSvc.getAllTransferedOrders();
		 model.addAttribute("lists",lists);
		 
		return "admin/query_order";
	}
	
	
	
	
	
	
	
}
