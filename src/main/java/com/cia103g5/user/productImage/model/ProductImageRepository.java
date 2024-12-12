package com.cia103g5.user.productImage.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


public interface ProductImageRepository extends JpaRepository<ProductImageVO, Integer>{

	//使用原生SQL語句(nativeQuery=true)進行查詢，傳入指商品編號的參數，作為SQL語句中的動態參數，並且設定where條件，取得is_primary為true的照片作為預設照片
	//自訂查詢預設照片的方法
	@Query(value = "SELECT * FROM prod_image WHERE prod_no = :prodNo AND is_primary = 1", nativeQuery = true)
	ProductImageVO findPrimaryImageByProdNo(@Param("prodNo") Integer prodNo);
	
	@Query(value="SELECT count(*) FROM prod_image WHERE prod_no = :prodNo AND is_primary = 0", nativeQuery = true)
	int findOthersPicsByProdNo(@Param("prodNo") Integer prodNo);
	
	@Query(value ="SELECT * FROM prod_image WHERE prod_no = :prodNo AND is_primary = 0", nativeQuery = true)
	List<ProductImageVO> findOtherPicVO (@Param("prodNo") Integer prodNo);
}
