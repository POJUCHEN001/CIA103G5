package com.cia103g5.user.product.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StoreServiceImpl implements StoreService {
	  private final StoreRepository storeRepository;

	    @Autowired
	    public StoreServiceImpl(StoreRepository storeRepository) {
	        this.storeRepository = storeRepository;
	    }

	   
	
	    @Override
	    public Page<ProductVO> getAllAvailableProducts(Pageable pageable) {
	        Page<ProductVO> products = storeRepository.findByStatus((byte) 1, pageable);

	        if (products == null || products.isEmpty()) {
	            return Page.empty();
	        }

	        return products; // 不過濾 null，如果確定不會返回 null
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

	         
	            if (product.getViewCount() == null) {
	                product.setViewCount(0);
	            }

	           
	            product.setViewCount(product.getViewCount() + 1);

	         
	            storeRepository.save(product);

	            return product;
	        } else {
	            throw new RuntimeException("找不到商品 ID: " + prodNo);
	        }
	    }
}
