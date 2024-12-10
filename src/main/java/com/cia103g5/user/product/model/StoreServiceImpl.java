package com.cia103g5.user.product.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public class StoreServiceImpl implements StoreService {
	  private final StoreRepository storeRepository;

	    @Autowired
	    public StoreServiceImpl(StoreRepository storeRepository) {
	        this.storeRepository = storeRepository;
	    }

	   
	    @Override
	    public Page<ProductVO> getAllAvailableProducts(Pageable pageable) {
	        // 回傳status=1的商品(上架中) 
	        return storeRepository.findByStatus((byte) 1, pageable);
	    }
	  
	    @Override
	    public List<ProductVO> searchProducts(String keyword) {
	        return storeRepository.findByProdNameContaining(keyword);
	    }

	    
	    @Override
	    public ProductVO getProductById(Integer prodNo) {
	        Optional<ProductVO> productOpt = storeRepository.findById(prodNo);

	        if (productOpt.isPresent()) {
	            ProductVO product = productOpt.get();

	        
	            product.setViewCount(product.getViewCount() == null ? 1 : product.getViewCount() + 1);
	            storeRepository.save(product);

	            return product;
	        } else {
	            throw new RuntimeException("找不到商品 ID: " + prodNo);
	        }
	    }
}
