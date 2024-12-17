package com.cia103g5.user.order.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cia103g5.user.order.model.dto.OrderSummaryDTO;
import com.cia103g5.user.order.model.dto.ShowReturnOrderDTO;
import com.cia103g5.user.order.model.dto.StatisticDTO;


public interface OrdersRepository extends JpaRepository<OrdersVO, Integer> {

	//使用ft_id去查詢該占卜師所有的訂單(FK)
	@Query(value="SELECT * FROM orders WHERE ft_id =:ftID",nativeQuery =true)
	List<OrdersVO> findAllOrdersByFtId(@Param("ftID") Integer ftId); 
	
	@Query(value="SELECT * FROM orders WHERE mem_id =:memID",nativeQuery =true)
	List<OrdersVO> findAllOrdersByMemId(@Param("memID") Integer memId); 
	
	@Modifying
	@Query("UPDATE OrdersVO o SET o.orderState =:newState WHERE o.orderNo =:orderNo ")
	int changeOrderStatusByOrderId(@Param("orderNo") Integer orderNo,@Param("newState") Byte state);
	

	@Modifying
	@Query("UPDATE OrdersVO o SET o.shipStatus =:newState WHERE o.orderNo =:orderNo ")
	int changeShipStatusByOrderId(@Param("orderNo") Integer orderNo,@Param("newState") Byte state);
	
	//DTO:跨表查詢時可用，new一個DTO物件(必須按照建構子順序填入實體變數)，填入的名稱是為VO當中實體變數的命名、以及用類別名稱	
	@Query("SELECT new com.cia103g5.user.order.model.dto.OrderSummaryDTO(o.orderNo, o.orderAmount, d.quantity, d.compositekey.productVO.prodNo, p.prodName, d.price, o.orderState) " +
		       "FROM OrdersVO o " +
		       "JOIN OrderDetailVO d ON o.orderNo = d.compositekey.ordersVO.orderNo " +
		       "JOIN ProductVO p ON d.compositekey.productVO.prodNo = p.prodNo " +
		       "WHERE d.compositekey.productVO.prodNo = ( " +
		       "    SELECT MIN(d2.compositekey.productVO.prodNo) " +
		       "    FROM OrderDetailVO d2 " +
		       "    WHERE d2.compositekey.ordersVO.orderNo = o.orderNo)")
	List<OrderSummaryDTO> findOrderSummaries();	
		
	//使用memId找到該會員所有的訂單摘要(回傳DTO)
	@Query("SELECT new com.cia103g5.user.order.model.dto.OrderSummaryDTO(o.orderNo, o.orderAmount, d.quantity, d.compositekey.productVO.prodNo, p.prodName, d.price, o.orderState) " +
		       "FROM OrdersVO o " +
		       "JOIN OrderDetailVO d ON o.orderNo = d.compositekey.ordersVO.orderNo " +
		       "JOIN ProductVO p ON d.compositekey.productVO.prodNo = p.prodNo " +
		       "WHERE d.compositekey.productVO.prodNo = ( " +
		       "    SELECT MIN(d2.compositekey.productVO.prodNo) " +
		       "    FROM OrderDetailVO d2 " +
		       "    WHERE d2.compositekey.ordersVO.orderNo = o.orderNo)"
		      +"AND o.memId.memberId =:memId")
	List<OrderSummaryDTO> findOrderSummariesByMemId(@Param("memId") Integer memId);	
	
	
	//依照訂單狀態查詢、返回符合條件之DTO
	@Query("SELECT new com.cia103g5.user.order.model.dto.OrderSummaryDTO(o.orderNo, o.orderAmount, d.quantity, d.compositekey.productVO.prodNo, p.prodName, d.price, o.orderState) " +
		       "FROM OrdersVO o " +
		       "JOIN OrderDetailVO d ON o.orderNo = d.compositekey.ordersVO.orderNo " +
		       "JOIN ProductVO p ON d.compositekey.productVO.prodNo = p.prodNo " +
		       "WHERE d.compositekey.productVO.prodNo = ( " +
		       "    SELECT MIN(d2.compositekey.productVO.prodNo) " +
		       "    FROM OrderDetailVO d2 " +
		       "    WHERE d2.compositekey.ordersVO.orderNo = o.orderNo)"
		      +" AND o.orderState =:orderState")
	List<OrderSummaryDTO>findOrderSummariesByOrderStatus(@Param("orderState") Byte state);
	
	
	
	//依照訂單狀態、該會員編號查詢、返回符合條件之DTO
	@Query("SELECT new com.cia103g5.user.order.model.dto.OrderSummaryDTO(o.orderNo, o.orderAmount, d.quantity, d.compositekey.productVO.prodNo, p.prodName, d.price, o.orderState) " +
		       "FROM OrdersVO o " +
		       "JOIN OrderDetailVO d ON o.orderNo = d.compositekey.ordersVO.orderNo " +
		       "JOIN ProductVO p ON d.compositekey.productVO.prodNo = p.prodNo " +
		       "WHERE d.compositekey.productVO.prodNo = ( " +
		       "    SELECT MIN(d2.compositekey.productVO.prodNo) " +
		       "    FROM OrderDetailVO d2 " +
		       "    WHERE d2.compositekey.ordersVO.orderNo = o.orderNo)"
		      +" AND o.orderState =:orderState"
		      +" AND o.memId.memberId =:memId")
	List<OrderSummaryDTO>findOrderSummariesByOrderStatusAndMemId(@Param("orderState") Byte state,@Param("memId") Integer memId);
	
	
	//依照物流狀態查詢、返回符合條件之DTO
	@Query("SELECT new com.cia103g5.user.order.model.dto.OrderSummaryDTO(o.orderNo, o.orderAmount, d.quantity, d.compositekey.productVO.prodNo, p.prodName, d.price, o.orderState) " +
		       "FROM OrdersVO o " +
		       "JOIN OrderDetailVO d ON o.orderNo = d.compositekey.ordersVO.orderNo " +
		       "JOIN ProductVO p ON d.compositekey.productVO.prodNo = p.prodNo " +
		       "WHERE d.compositekey.productVO.prodNo = ( " +
		       "    SELECT MIN(d2.compositekey.productVO.prodNo) " +
		       "    FROM OrderDetailVO d2 " +
		       "    WHERE d2.compositekey.ordersVO.orderNo = o.orderNo)"
		      +" AND o.shipStatus =:shipStatus")
	List<OrderSummaryDTO>findOrderSummariesByShipStatus(@Param("shipStatus") Byte status);
	
	
	
	//依照物流狀態、該會員編號查詢、返回符合條件之DTO
	@Query("SELECT new com.cia103g5.user.order.model.dto.OrderSummaryDTO(o.orderNo, o.orderAmount, d.quantity, d.compositekey.productVO.prodNo, p.prodName, d.price, o.orderState) " +
		       "FROM OrdersVO o " +
		       "JOIN OrderDetailVO d ON o.orderNo = d.compositekey.ordersVO.orderNo " +
		       "JOIN ProductVO p ON d.compositekey.productVO.prodNo = p.prodNo " +
		       "WHERE d.compositekey.productVO.prodNo = ( " +
		       "    SELECT MIN(d2.compositekey.productVO.prodNo) " +
		       "    FROM OrderDetailVO d2 " +
		       "    WHERE d2.compositekey.ordersVO.orderNo = o.orderNo)"
		      +" AND o.shipStatus =:shipStatus"
		      +" AND o.memId.memberId =:memId")
	List<OrderSummaryDTO>findOrderSummariesByShipStatusAndMemId(@Param("shipStatus") Byte status,@Param("memId") Integer memId);
	

	//每個占卜師的訂單統計結果放進DTO，回傳多個DTO(list)
//	@Query("SELECT new com.order.model.StatisticDTO(o.ftId, count(o), sum(o.realAmount)) "
//			+" FROM OrdersVO o GROUP BY o.ftId ")
//	List<StatisticDTO> getAllOrderStatisticsGroupByFtid();
	
	//管理員統計每月的訂單(取得當前月份前一個月的訂單統計)
	@Query("SELECT new com.cia103g5.user.order.model.dto.StatisticDTO(count(o),sum(o.realAmount) ) "
		   + " FROM OrdersVO o WHERE o.endedTime BETWEEN :startOfLastMonth AND :endOfLastMonth")
	StatisticDTO getLastMonthOrderStatistics(@Param("startOfLastMonth") Timestamp startOfLastMonth,@Param("endOfLastMonth") Timestamp endOfLastMonth);
	
	
	//占卜師統計前一個月的訂單
	@Query("SELECT new com.cia103g5.user.order.model.dto.StatisticDTO(count(o),sum(o.realAmount) ) "
			   + " FROM OrdersVO o WHERE (o.endedTime BETWEEN :startOfLastMonth AND :endOfLastMonth) AND (o.ftId.ftId =:ftId)")
	StatisticDTO getLastMonthOrderStatisticsByFtId(@Param("startOfLastMonth") Timestamp startOfLastMonth,@Param("endOfLastMonth") Timestamp endOfLastMonth,@Param("ftId")Integer ftId);
	
	
	//占卜師統計所有的訂單
	@Query("SELECT new com.cia103g5.user.order.model.dto.StatisticDTO(count(o),sum(o.realAmount)) FROM OrdersVO o WHERE o.ftId.ftId =:ftId")
	StatisticDTO getAllOrderStatisticsByFtId(@Param("ftId")Integer ftId);
	
	//用orderNo取得一個ShowReturnOrderDTO，一筆資料
	@Query("SELECT new com.cia103g5.user.order.model.dto.ShowReturnOrderDTO(o.orderNo,o.memId.memberId,o.ftId.ftId,o.payment,o.orderState,o.endedTime)"
			+ " FROM OrdersVO o WHERE o.orderNo =:orderNo")
	ShowReturnOrderDTO getReturnOrderByorderNo(@Param("orderNo") Integer orderNo);
	
	//訂單的複合查詢
	@Query("SELECT o FROM OrdersVO o WHERE " +
	"( (:startDate IS NOT NULL AND :endDate IS NOT NULL AND o.createdTime BETWEEN :startDate AND :endDate)" +
	"  OR (:startDate IS NULL AND :endDate IS NULL)) AND " +
	"(:memId is NULL OR o.memId.memberId=:memId ) AND " +
	"(:orderNo is NULL OR o.orderNo =:orderNo ) AND " +
	"(:orderState is NULL OR o.orderState =:orderState ) AND " +
	"(:shipStatus is NULL OR o.shipStatus =:shipStatus)")
	List<OrdersVO> getOrdersByCompositeQuery(@Param("startDate")Timestamp startDate
											,@Param("endDate")Timestamp endDate
											,@Param("memId") Integer memId
											,@Param("orderNo")Integer orderNo
											,@Param("orderState") Byte orderState
											,@Param("shipStatus")Byte shipStatus);
	
	
	
//	//藉由占卜師編號做訂單的復合查詢
	@Query("SELECT o FROM OrdersVO o WHERE " +
			"( (:startDate IS NOT NULL AND :endDate IS NOT NULL AND o.createdTime BETWEEN :startDate AND :endDate)" +
			"  OR (:startDate IS NULL AND :endDate IS NULL)) AND " +
			"(:memId is NULL OR o.memId.memberId=:memId ) AND " +
			"(:orderNo is NULL OR o.orderNo =:orderNo ) AND " +
			"(:orderState is NULL OR o.orderState =:orderState ) AND " +
			"(:shipStatus is NULL OR o.shipStatus =:shipStatus) AND " +
			"(o.ftId.ftId =:ftId)")
	List<OrdersVO> getOrdersByCompositeQueryByFtId(@Param("startDate")Timestamp startDate
			,@Param("endDate")Timestamp endDate
			,@Param("memId") Integer memId
			,@Param("orderNo")Integer orderNo
			,@Param("orderState") Byte orderState
			,@Param("shipStatus")Byte shipStatus
			,@Param("ftId") Integer ftId);

	
	//管理員複合查詢訂單(重要:JPQL映射要返回物件實體就要查詢全部資料，如果只要取部分資料就要用DTO，DTO實體變數的型別要對應VO的型態才可以建購)
	@Query("SELECT o "
		+" FROM OrdersVO o WHERE"
		+"( :orderNo IS NULL OR o.orderNo =:orderNo) AND" 
		+"( :memId IS NULL OR o.memId.memberId =:memId) AND "
		+"( :ftId IS NULL OR o.ftId.ftId =:ftId)")
	List<OrdersVO> getOrdersByCompositeByAdmin(@Param("orderNo")Integer orderNo,@Param("memId")Integer memId,@Param("ftId")Integer ftId);
	
	
	//查第一筆訂單的年份
	@Query("SELECT YEAR(MIN(o.createdTime)) FROM OrdersVO o")
	Integer findFirstOrderYear();
	
	
	//查該占卜師第一筆訂單的年份
	@Query("SELECT YEAR(MIN(o.createdTime)) FROM OrdersVO o WHERE o.ftId.ftId =:ftId")
	Integer findFirstOrderYearByFtId(@Param("ftId") Integer ftId);

	
	@Modifying
	@Query(value="UPDATE orders SET ended_time =:time WHERE order_no =:orderNo",nativeQuery =true)
	void updateEndedTimeForFinishedOrder(@Param("orderNo")Integer orderNo,@Param("time") Timestamp time);
	
}
	
