package com.cia103g5.user.store.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cia103g5.user.product.model.ProductVO;
import com.cia103g5.user.product.model.StoreService;


@RestController
@RequestMapping("/fixlife/store")
public class StoreController {
	
	private final StoreService storeService;

    @Autowired
    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }
    

   
    @GetMapping("/available")
    public Page<ProductVO> getAllAvailableProducts(Pageable pageable) {
        return storeService.getAllAvailableProducts(pageable);
    }
    
    // 顯示商品列表頁面
    @GetMapping("/products")
    public String showProductList(@RequestParam(defaultValue = "0") int page, Model model) {
        
        Page<ProductVO> productPage = storeService.getAllAvailableProducts(PageRequest.of(page, 10)); // 每頁顯示 10 個商品

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);

        return "product_list"; // 對應到 resources/templates/product_list.html
    }
    


    // 搜尋商品
    @GetMapping("/search")
    public List<ProductVO> searchProducts(@RequestParam String keyword) {
        return storeService.searchProducts(keyword);
    }

    // 查看商品詳情
    @GetMapping("/{prodNo}")
    public ProductVO getProductById(@PathVariable Integer prodNo) {
        return storeService.getProductById(prodNo);
    }

  //獲取購物車商品
//    @GetMapping("/{userId}")
//    public List<CartVO> getCartItems(@PathVariable Integer userId) {
//        return cartService.getCartItemsByUserId(userId);
//    }
    
    
    
}
