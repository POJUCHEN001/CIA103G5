package com.cia103g5.user.order.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cia103g5.user.order.model.dto.AdminOrderTransferDTO;
import com.cia103g5.user.order.model.dto.OrderSummaryDTO;
import com.cia103g5.user.order.model.dto.ScoringDTO;
import com.cia103g5.user.order.model.dto.StatisticDTO;
import com.cia103g5.user.order.model.dto.TransferOrderDTO;
import com.cia103g5.user.order.util.TimeTransfer;
import com.cia103g5.user.order.util.Transfer;
import com.cia103g5.user.orderDetails.model.OrderDetailService;
import com.cia103g5.user.productImage.model.ProductImageService;



@Service("ordersService")
public class OrdersService {

	@Autowired
	OrdersRepository repository;
	
	@Autowired
    private SessionFactory sessionFactory;
	
	@Autowired
    private ProductImageService productImageService;
	
	@Autowired
	private Transfer transfer;
	
	@Autowired
	private TimeTransfer timeTransfer;
	
	@Autowired
	private OrderDetailService detailSvc;
	
	public void addOrder(OrdersVO orderVO) {
		if(!repository.existsById(orderVO.getOrderNo())) {
			repository.save(orderVO);
		}
		
	}
	
	public void updateOrder(OrdersVO orderVO) {
		if(repository.existsById(orderVO.getOrderNo())) {
			repository.save(orderVO);
		}		
	}
	
	public void deleteOrder(Integer orderNo) {
		if(repository.existsById(orderNo)) {
			repository.deleteById(orderNo);
		}
	}
	
	public OrdersVO getOneOrder(Integer orderNo) {
		Optional<OrdersVO> optional =repository.findById(orderNo);
//		OrdersVO order = optional.get();
		return optional.orElse(null);
	}
	
	public List<OrdersVO> getAll(){
		return repository.findAll();
	}
	
	//轉換好時間、付款方式、物流狀態、訂單狀態，回傳全部訂單資料的DTO
	public List<AdminOrderTransferDTO> getAllTransferedOrders(){
		List<AdminOrderTransferDTO> newlist = new ArrayList<AdminOrderTransferDTO>();
		List<OrdersVO> originList =repository.findAll();
		
		for(OrdersVO orderVO : originList) {
			AdminOrderTransferDTO DTO =new AdminOrderTransferDTO();
			DTO.setOrderNo(orderVO.getOrderNo());
			DTO.setMemId(orderVO.getMemId().getMemberId());
			DTO.setFtId(orderVO.getFtId().getFtId());
			DTO.setOrderAmount(orderVO.getOrderAmount());
			DTO.setRealAmount(orderVO.getRealAmount());
			

			String orderStateString= transfer.transferOrderState(orderVO.getOrderState());
			DTO.setOrderState(orderStateString);
			
			String shipStateString =transfer.transferShipStatus(orderVO.getShipStatus());			
			DTO.setShipStatus(shipStateString);
			
			String paymentString=transfer.transferPayment(orderVO.getPayment());
			DTO.setPayment(paymentString);
			
			if(orderVO.getCreatedTime()!=null) {
				DTO.setCreatedTime(orderVO.getCreatedTime().toLocalDateTime().toLocalDate());
			}else {
				DTO.setCreatedTime(null);
			}
			
			if(orderVO.getEndedTime()!=null) {
				DTO.setEndedTime(orderVO.getEndedTime().toLocalDateTime().toLocalDate());
			}else {
				DTO.setEndedTime(null);
			}
						
			
			newlist.add(DTO);
		}
		
		return newlist;
		
	}
	
	//管理員複合查詢
	public List<AdminOrderTransferDTO> compositeQueryOrderByAdmin(Integer orderNo,Integer memId,Integer ftId){
		List<OrdersVO> originList =repository.getOrdersByCompositeByAdmin(orderNo, memId, ftId);
		List<AdminOrderTransferDTO> DTOList =new ArrayList<AdminOrderTransferDTO>();
	
		for(OrdersVO orderVO : originList) {
			AdminOrderTransferDTO DTO =new AdminOrderTransferDTO();
			DTO.setOrderNo(orderVO.getOrderNo());
			DTO.setMemId(orderVO.getMemId().getMemberId());
			DTO.setFtId(orderVO.getFtId().getFtId());
			DTO.setOrderAmount(orderVO.getOrderAmount());
			DTO.setRealAmount(orderVO.getRealAmount());
			
			String orderStateString= transfer.transferOrderState(orderVO.getOrderState());
			DTO.setOrderState(orderStateString);
			
			String shipStateString =transfer.transferShipStatus(orderVO.getShipStatus());			
			DTO.setShipStatus(shipStateString);
			
			String paymentString=transfer.transferPayment(orderVO.getPayment());
			DTO.setPayment(paymentString);
			
			if(orderVO.getCreatedTime()!=null) {
				DTO.setCreatedTime(orderVO.getCreatedTime().toLocalDateTime().toLocalDate());
			}else {
				DTO.setCreatedTime(null);
			}
			
			if(orderVO.getEndedTime()!=null) {
				DTO.setEndedTime(orderVO.getEndedTime().toLocalDateTime().toLocalDate());
			}else {
				DTO.setEndedTime(null);
			}
			
			DTOList.add(DTO);
		}
		return DTOList;
	}
	
	
	//傳入ft_id(占卜師編號)，找到該占卜師的所有訂單
	public List<OrdersVO> getAllByFtId(Integer ftId){
		return repository.findAllOrdersByFtId(ftId);
	}
	
	//傳入mem_id(會員編號)，找到該會員所有訂單
	public List<OrdersVO> getAllByMemId(Integer memId){
		return repository.findAllOrdersByMemId(memId);
	}
	
	//變更訂單狀態的方法	
	@Transactional
	public Boolean alterOrderState(Integer orderId,Byte newStatus) {
		
		if(repository.changeOrderStatusByOrderId(orderId, newStatus)!=0) {
			return true;
		}else {
			return false;
		}
		
	}
	
	//變更物流狀態的方法	
		@Transactional
		public int alterShipStatus(Integer orderId,Byte newStatus) {
			
			if(repository.changeShipStatusByOrderId(orderId, newStatus)!=0) {
				return 1;
			}else {
				return 0;
			}
			
		}
	
	
	//跨表查詢，回傳多個DTO
	public List<OrderSummaryDTO> getOrderSummary(){
		return repository.findOrderSummaries();
	}
	
	//查詢某位會員的所有訂單(回傳多個DTO-顯示訂單摘要資訊)
	public List<OrderSummaryDTO> getOrderSummaryByMemId(Integer memId){
		return repository.findOrderSummariesByMemId(memId);
	}
	
	//依照訂單狀態查詢
	public List<OrderSummaryDTO> getOrderSummaryByOrderState(Byte state){
		
		List<OrderSummaryDTO> list =repository.findOrderSummariesByOrderStatus(state);
		
		for(OrderSummaryDTO oneDTO:list) {
			List<ScoringDTO> comments =detailSvc.getProductCommentByOrderNo(oneDTO.getOrderNo());
			System.out.println("如果有評論的話，每個商品的評分都是必填，所以可以取得評分數值:"+comments.get(0).getRateScore());
			if(comments.get(0).getRateScore()!=null) {
				oneDTO.setComment(true);
			}else {
				oneDTO.setComment(false);
			}
			
		}
		return list;
	}
	
	//依照訂單狀態、會員編號查詢
	public List<OrderSummaryDTO> getOrderSummaryByOrderStateAndMemId(Byte state,Integer memId){
		List<OrderSummaryDTO> list =repository.findOrderSummariesByOrderStatusAndMemId(state, memId);
		
		for(OrderSummaryDTO oneDTO:list) {
			List<ScoringDTO> comments =detailSvc.getProductCommentByOrderNo(oneDTO.getOrderNo());
			System.out.println("如果有評論的話，每個商品的評分都是必填，所以可以取得評分數值:"+comments.get(0).getRateScore());
			if(comments.get(0).getRateScore()!=null) {
				oneDTO.setComment(true);
			}else {
				oneDTO.setComment(false);
			}
			
		}
		
		return list;
	}
	
	//依照物流狀態查詢
	public List<OrderSummaryDTO> getOrderSummaryByShipStatus(Byte status){
		
		List<OrderSummaryDTO> list =repository.findOrderSummariesByShipStatus(status);
		return list;
	}
	
	//依照物流狀態、會員編號查詢
	public List<OrderSummaryDTO> getOrderSummaryByShipStatusAndMemId(Byte status,Integer memId){		
		List<OrderSummaryDTO> list =repository.findOrderSummariesByShipStatusAndMemId(status, memId);
		return list;
		
	}
	
	
	//管理員查詢訂單統計，用ft_id分組，回傳多個包好的DTO(list)
//	public List<StatisticDTO> getAllOrderStatisticsGroupByFtid(){
//		return repository.getAllOrderStatisticsGroupByFtid();
//	}
	
	//管理員查詢訂單統計，回傳上個月的訂單統計
	public StatisticDTO getLastMonthStatistics() {
		
		 LocalDateTime now = LocalDateTime.now();
		 LocalDateTime firstDayOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
		 LocalDateTime lastDayOfLastMonth = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
		 
		 Timestamp startOfLastMonth = Timestamp.valueOf(firstDayOfLastMonth);
	     Timestamp endOfLastMonth = Timestamp.valueOf(lastDayOfLastMonth);
		 
		 StatisticDTO DTO =repository.getLastMonthOrderStatistics(startOfLastMonth, endOfLastMonth);
		 if(DTO.getTotalAmount()!=null) {
	    	 DTO.setRevenue(DTO.getTotalAmount()*0.05);
	 		DTO.setSettlement(( DTO.getTotalAmount() ) - (DTO.getTotalAmount()*0.05 ) );
	    }else {
	    	DTO.setRevenue(0.0);
	    	DTO.setSettlement(0.0);
	    }
		 return DTO;
		 
	}
	
	//管理員選擇年月查詢，回傳該年該月份的統計
	public StatisticDTO getOrderChooseByYearAndMonth(Integer year,Integer month) {
		//取得該年月的第一天
		LocalDate firstDayOfMonth =LocalDate.of(year, month, 1);
		//取得該月的最後一天
		LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
		Timestamp startOfMonth = Timestamp.valueOf(firstDayOfMonth.atStartOfDay());//轉換成Timestamp
	    Timestamp endOfMonth = Timestamp.valueOf(lastDayOfMonth.atTime(23, 59, 59));//轉換成Timestamp
		
	    StatisticDTO DTO =repository.getLastMonthOrderStatistics(startOfMonth, endOfMonth);
	    
	    if(DTO.getTotalAmount()!=null) {
	    	 DTO.setRevenue(DTO.getTotalAmount()*0.05);
	 		DTO.setSettlement(( DTO.getTotalAmount() ) - (DTO.getTotalAmount()*0.05 ) );
	    }else {
	    	DTO.setRevenue(0.0);
	    	DTO.setSettlement(0.0);
	    }
	    
	   
		 
	    return DTO;
	}
	
	
	//取得占卜師上個月的訂單統計
	public StatisticDTO getLastMonthStatisticsByFt(Integer ftId) {
		 LocalDateTime now = LocalDateTime.now();
		 LocalDateTime firstDayOfLastMonth = now.minusMonths(1).withDayOfMonth(1);
		 LocalDateTime lastDayOfLastMonth = now.minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
		 
		 Timestamp startOfLastMonth = Timestamp.valueOf(firstDayOfLastMonth);
	     Timestamp endOfLastMonth = Timestamp.valueOf(lastDayOfLastMonth);
	     
	     StatisticDTO DTO =repository.getLastMonthOrderStatisticsByFtId(startOfLastMonth, endOfLastMonth, ftId);
		 if(DTO.getTotalAmount()!=null) {
	    	 DTO.setRevenue(DTO.getTotalAmount()*0.05);
	 		DTO.setSettlement(( DTO.getTotalAmount() ) - (DTO.getTotalAmount()*0.05 ) );
	    }else {
	    	DTO.setRevenue(0.0);
	    	DTO.setSettlement(0.0);
	    	DTO.setTotalAmount(0L);
	    }
		 return DTO;	     
	     
	}
	
	//取得占卜師所有的訂單統計
	public StatisticDTO getAllStatisticByFtId(Integer ftId) {
		 StatisticDTO DTO =repository.getAllOrderStatisticsByFtId(ftId);
		 
		 if(DTO.getTotalAmount()!=null) {
	    	 DTO.setRevenue(DTO.getTotalAmount()*0.05);
	 		DTO.setSettlement(( DTO.getTotalAmount() ) - (DTO.getTotalAmount()*0.05 ) );
	    }else {
	    	DTO.setRevenue(0.0);
	    	DTO.setSettlement(0.0);
	    	DTO.setTotalAmount(0L);
	    }
		 return DTO;
		
	}
	
	//占卜師選擇年月，回傳訂單統計的DTO
	public StatisticDTO orderStatisticsByChooseFromFt(Integer year,Integer month,Integer ftId) {
		//取得該年月的第一天
		LocalDate firstDayOfMonth =LocalDate.of(year, month, 1);
		//取得該月的最後一天
		LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());
		Timestamp startOfMonth = Timestamp.valueOf(firstDayOfMonth.atStartOfDay());//轉換成Timestamp
	    Timestamp endOfMonth = Timestamp.valueOf(lastDayOfMonth.atTime(23, 59, 59));//轉換成Timestamp
		
	    StatisticDTO DTO =repository.getLastMonthOrderStatisticsByFtId(startOfMonth, endOfMonth, ftId);
	    
	    if(DTO.getTotalAmount()!=null) {
	    	 DTO.setRevenue(DTO.getTotalAmount()*0.05);
	 		DTO.setSettlement(( DTO.getTotalAmount() ) - (DTO.getTotalAmount()*0.05 ) );
	    }else {
	    	DTO.setRevenue(0.0);
	    	DTO.setSettlement(0.0);
	    	DTO.setTotalAmount(0L);
	    }
	       
		System.out.println("將statisticDTO印出來看:"+DTO.toString()); 
	    return DTO;
	}
	
	

	//訂單的複合查詢
	public List<TransferOrderDTO> getOrdersByCompositeQuery(Timestamp startDate,Timestamp  endDate,Integer memId,Integer orderNo,Byte orderState,Byte shipStatus){
		List<OrdersVO> list =repository.getOrdersByCompositeQuery(startDate, endDate, memId, orderNo, orderState, shipStatus);
		
		//用來裝要傳送給前端的DTO，將手動裝
		List<TransferOrderDTO> sendList =new ArrayList<TransferOrderDTO>();
		
		for(OrdersVO VO :list) {
			TransferOrderDTO DTO =new TransferOrderDTO();
			
			DTO.setOrderNo(VO.getOrderNo());
			DTO.setMemId(VO.getMemId().getMemberId());
			DTO.setPayment(VO.getPayment());
			DTO.setOrderAmount(VO.getOrderAmount());
			DTO.setShipStatus(VO.getShipStatus());
			DTO.setOrderState(VO.getOrderState());
			
			//轉換時間
			DTO.setCreatedTime(VO.getCreatedTime().toLocalDateTime().toLocalDate());
			
			if(VO.getEndedTime()!=null) {
				DTO.setEndedTime(VO.getEndedTime().toLocalDateTime().toLocalDate());
			}		
			
			//每次迭代都new DTO，存入list
			sendList.add(DTO);
		}
		
		
		return sendList;
	}
	
	//占卜師訂單的複合查詢
		public List<TransferOrderDTO> getOrdersByCompositeQueryByFtId(Timestamp startDate,Timestamp  endDate,Integer memId,Integer orderNo,Byte orderState,Byte shipStatus,Integer ftId){
			
			List<OrdersVO> list =repository.getOrdersByCompositeQueryByFtId(startDate, endDate, memId, orderNo, orderState, shipStatus,ftId);
			
			//用來裝要傳送給前端的DTO，將手動裝
			List<TransferOrderDTO> sendList =new ArrayList<TransferOrderDTO>();			

			for(OrdersVO VO :list) {
				TransferOrderDTO DTO =new TransferOrderDTO();
				
				DTO.setOrderNo(VO.getOrderNo());
				DTO.setMemId(VO.getMemId().getMemberId());
				DTO.setPayment(VO.getPayment());
				DTO.setOrderAmount(VO.getOrderAmount());
				DTO.setShipStatus(VO.getShipStatus());
				DTO.setOrderState(VO.getOrderState());
				
				//轉換時間
				DTO.setCreatedTime(VO.getCreatedTime().toLocalDateTime().toLocalDate());
				
				if(VO.getEndedTime()!=null) {
					DTO.setEndedTime(VO.getEndedTime().toLocalDateTime().toLocalDate());
				}		
				
				//每次迭代都new DTO，存入list
				sendList.add(DTO);
			}			
			
			return sendList;
		}
	
	
	//回傳第一筆訂單的年份
		public Integer getFirstOrderYear() {
		    return repository.findFirstOrderYear();
		}
		
		
	//回傳一位占卜師第一筆訂單的年份
		public Integer getFirstOrderYearFromFtId(Integer ftId) {
			return repository.findFirstOrderYearByFtId(ftId);
		}


		
		
		
}
