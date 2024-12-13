package com.cia103g5.user.product.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreService {

	Page<ProductVO> getAllAvailableProducts(Pageable pageable);

 
    List<ProductVO> searchProducts(String keyword);



    ProductVO getProductById(Integer prodNo);
}
