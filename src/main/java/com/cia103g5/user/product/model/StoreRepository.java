package com.cia103g5.user.product.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<ProductVO, Integer> {

	
    List<ProductVO> findByStatus(Byte status);

  
    List<ProductVO> findByProdNameContaining(String keyword);
	
    Page<ProductVO> findByStatus(Byte status, Pageable pageable);
}
