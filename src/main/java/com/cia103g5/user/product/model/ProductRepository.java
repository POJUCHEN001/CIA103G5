package com.cia103g5.user.product.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductVO, Integer>{
	
	//查詢單一占卜師的所有商品(排除刪除項目)
	@Query(value="SELECT * FROM product WHERE ft_id =:ftID AND status != 2",nativeQuery =true)
	List<ProductVO> findAllByFtIdExcludeDelete(@Param("ftID") Integer ftid);

	//查詢所有商品(不指定占卜師)(排除刪除項目)
	@Query(value ="SELECT * FROM product WHERE status != 2",nativeQuery =true)
	List<ProductVO> findAllExcludeDelete();
	
	
	//針對status欄位做更新(JPA預設的update method會更新全部欄位)	
	@Modifying
	@Query(value="UPDATE product SET status = 2 WHERE prod_no =:prodNO",nativeQuery =true)
	void updateOneStatus(@Param("prodNO") Integer prodno);
	
	//透過status查出所有符合條件的product，回傳list
	@Query("SELECT p FROM ProductVO p WHERE p.status =:status")
	List<ProductVO> getAllProductByStatus(@Param("status") Byte status); //前端先將文字依照狀態轉換成數字，再傳進來
	
	//透過status查出所有符合條件的product，回傳list
	@Query("SELECT p FROM ProductVO p WHERE p.status =:status AND p.ftId.ftId =:ftId")
	List<ProductVO> getAllProductByStatusAndFtId(@Param("status") Byte status,@Param("ftId") Integer ftId);
	
	
	//複合查詢
	@Query("SELECT p FROM ProductVO p WHERE " +
	           "(NULL=:status OR p.status = :status) AND " +
	           "(NULL=:prodName OR p.prodName LIKE CONCAT('%', :prodName, '%') ) AND " +
	           "(NULL=:prodNo OR p.prodNo = :prodNo)" )
	List<ProductVO> findProductsByMutiCondition(@Param("status") Byte status,
	                               @Param("prodName") String prodName,
	                               @Param("prodNo") Integer prodNo);
	
	
	//複合查詢by ft
//	@Query("SELECT p FROM ProductVO p WHERE " +
//	           "(NULL=:status OR p.status = :status) AND " +
//	           "(NULL=:prodName OR p.prodName LIKE CONCAT('%', :prodName, '%') ) AND " +
//	           "(NULL=:prodNo OR p.prodNo = :prodNo) AND "+
//	           "(p.ftId.ftId =:ftId)")
//	List<ProductVO> findProductsByMutiConditionByFtId
//								  (@Param("status") Byte status,
//	                               @Param("prodName") String prodName,
//	                               @Param("prodNo") Integer prodNo,
//	                               @Param("ftId")Integer ftId);
//	
	
	@Query(value = "SELECT * FROM product WHERE " +
            "(:status IS NULL OR status = :status) AND " +
            "(:prodName IS NULL OR prod_name LIKE CONCAT('%', :prodName, '%')) AND " +
            "(:prodNo IS NULL OR prod_no = :prodNo) AND " +
            "ft_id = :ftId", 
		    nativeQuery = true)
		List<ProductVO> findProductsByMutiConditionByFtId(
		 @Param("status") Byte status,
		 @Param("prodName") String prodName,
		 @Param("prodNo") Integer prodNo,
		 @Param("ftId") Integer ftId);

	
}
