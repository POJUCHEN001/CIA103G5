package com.cia103g5.user.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cia103g5.user.product.model.ProductVO;
import com.cia103g5.user.product.model.StoreServiceImpl;

@Controller
@RequestMapping("/store")
public class StoreController {

	private final StoreServiceImpl storeServiceImpl;

	@Autowired
	public StoreController(StoreServiceImpl storeServiceImpl) {
		this.storeServiceImpl = storeServiceImpl;
	}

	@GetMapping("/available")
	public Page<ProductVO> getAllAvailableProducts(Pageable pageable) {
		return storeServiceImpl.getAllAvailableProducts(pageable);
	}

	@GetMapping("/products")
	public String showProductList(@RequestParam(defaultValue = "0") int page, Model model) {
	    int pageSize = 10;
	    Pageable pageable = PageRequest.of(page, pageSize);
	    Page<ProductVO> storePage = storeServiceImpl.getAllAvailableProducts(pageable);

	    int totalPages = storePage.getTotalPages();
	    if (page >= totalPages) {
	        page = totalPages - 1; 
	    } else if (page < 0) {
	        page = 0; 
	    }

	
	    System.out.println("商品數據：" + storePage.getContent());
	    
	    model.addAttribute("products", storePage.getContent());
	    model.addAttribute("totalPages", totalPages);
	    model.addAttribute("currentPage", page);
	    model.addAttribute("pageSize", pageSize);
	    return "store";
	}

	@GetMapping("/search")
	public List<ProductVO> searchProducts(@RequestParam String keyword) {
		return storeServiceImpl.searchProducts(keyword);
	}

	@GetMapping("/productdetail/{id}")
	public String productDetail(@PathVariable("id") Integer id, Model model) {
		ProductVO product = storeServiceImpl.getProductById(id);
		model.addAttribute("product", product);
		return "productdetail";
	}

	// 獲取購物車商品
//    @GetMapping("/{userId}")
//    public List<CartVO> getCartItems(@PathVariable Integer userId) {
//        return cartService.getCartItemsByUserId(userId);
//    }

}
