package com.cia103g5.user.product.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreService {
    // 列出所有上架商品
	Page<ProductVO> getAllAvailableProducts(Pageable pageable);

    // 關鍵字搜尋商品
    List<ProductVO> searchProducts(String keyword);


    // 瀏覽商品詳情
    ProductVO getProductById(Integer prodNo);
}
