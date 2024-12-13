package com.cia103g5.user.orderDetails.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cia103g5.user.order.model.dto.ScoringDTO;
import com.cia103g5.user.orderDetails.model.OrderDetailVO.CompositeDetail;




//繼承JpaRepository，自動用hibernate實作基本CRUD
public interface OrderDetailRepository extends JpaRepository<OrderDetailVO, CompositeDetail> {

//	存在該值回傳1，返之回傳0
	boolean existsById(CompositeDetail compositeDetail);

	//預設deleteById的參數型別為ID，複合主鍵要自訂複合主鍵型別的方法
	void deleteById(CompositeDetail compositeDetail);

	
	//使用order_no取得所有訂單明細(JPQL寫法，JPQL=java persistence query language)
	@Query("SELECT o FROM OrderDetailVO o WHERE o.compositekey.ordersVO.orderNo =:orderNo")
	List<OrderDetailVO> findAllDetailsByOrderNo(@Param("orderNo")Integer orderNo);
	
	
	//只針對rate_content & rate_score做新增的方法(指定條件為:orderNo、productNo)
	@Modifying
	@Query(value = "UPDATE order_detail "
	             + "SET rate_content = :rateContent, rate_score = :rateScore "
	             + "WHERE order_no = :orderNo AND prod_no = :prodNo", nativeQuery = true)
	int addProductComment(@Param("orderNo") Integer orderNo,
	                       @Param("prodNo") Integer prodNo,
	                       @Param("rateContent") String rateContent,
	                       @Param("rateScore") Integer rateScore);

		
	//回傳一個評分資訊的DTO集合
	@Query("SELECT new com.cia103g5.user.order.model.dto.ScoringDTO(d.rateScore,d.rateContent,"
			+ "p.prodName,p.prodNo)"
			+ " FROM OrderDetailVO d"
			+ " JOIN ProductVO p ON d.compositekey.productVO.prodNo = p.prodNo"
			+ " WHERE d.compositekey.ordersVO.orderNo =:orderNo")
	List<ScoringDTO> findScoringInfoByOrderNo(@Param("orderNo") Integer orderNo);
	
	
	//只針對is_return、return_info、return_photo做update的方法	
	@Transactional
	@Modifying
	@Query(value= "UPDATE order_detail SET is_return =:isReturn,return_info =:returnInfo,return_photo =:returnPhoto"
		 +" WHERE order_no = :orderNo AND prod_no = :prodNo",nativeQuery = true)
	int updateReturnInfo(@Param("isReturn") Byte isReturn,
							@Param("returnInfo") String returnInfo,
							@Param("returnPhoto") byte[] returnPhoto,
							@Param("orderNo") Integer orderNo,
							@Param("prodNo") Integer prodNo);
	
	//查出所有訂單明細中is_return=1的的訂單編號，用訂單編號做分組
	@Query(value="SELECT order_no FROM order_detail WHERE is_return = 1 GROUP BY order_no",nativeQuery = true)
	List<Integer> getAllReturnOrderNo ();
	
	@Query(value="SELECT return_info FROM order_detail WHERE is_return =1 AND order_no =:orderNo LIMIT 1",nativeQuery = true)
	String getOneReturnInfoStringByOrderNoAndIsReturnLimit1(@Param("orderNo") Integer orderNo);
	
	

}
