package com.cia103g5.user.product.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<ProductVO, Integer> {

	 // 查詢所有上架的商品
    List<ProductVO> findByStatus(Byte status);

    // 商品名稱模糊搜尋
    List<ProductVO> findByProdNameContaining(String keyword);
	
    Page<ProductVO> findByStatus(Byte status, Pageable pageable);
}
