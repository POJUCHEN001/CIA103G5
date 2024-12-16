package com.cia103g5.user.store.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cia103g5.user.ft.model.FtService;
import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.product.model.ProductVO;
import com.cia103g5.user.product.model.StoreServiceImpl;
import com.cia103g5.user.productCollection.model.ProductCollectionServiceImpl;
import com.cia103g5.user.productCollection.model.ProductCollectionVO;
import com.cia103g5.user.productImage.model.ProductImageService;
import com.cia103g5.user.productImage.model.ProductImageVO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/store") // 前綴路徑 /store
public class StoreController {

    @Autowired
    StoreServiceImpl storeServiceImpl;
	@Autowired
	ProductImageService prodimgSvc;
	@Autowired
	ProductCollectionServiceImpl productCollectionService;
	@Autowired
	private FtService ftService;
	
//	@GetMapping("/")
//	public String toIndex() {
//		return "index";
//	}
	
	@GetMapping("/products")
	public String showAvailableProducts(@RequestParam(defaultValue = "0") int page, 
	                                    @RequestParam(required = false) Integer memId, 
	                                    Model model) {
	    int pageSize = 10; // 每頁顯示的商品數量
	    Pageable pageable = PageRequest.of(page, pageSize);
	    Page<ProductVO> productPage = storeServiceImpl.getAllAvailableProducts(pageable);

	    if (productPage.isEmpty()) {
	        model.addAttribute("message", "目前沒有商品上架。");
	    } else {
	        model.addAttribute("products", productPage.getContent());
	    }

	    // 如果有傳遞用戶 ID，獲取用戶的收藏記錄
	    if (memId != null) {
	        List<ProductCollectionVO> collections = productCollectionService.findByMemId(memId);
	        model.addAttribute("collections", collections);
	    }

	    model.addAttribute("totalPages", productPage.getTotalPages());
	    model.addAttribute("currentPage", page);
	    model.addAttribute("pageSize", pageSize);

	    return "store"; 
	}

	
    @GetMapping("/image/{prodNo}")
    public void showProductImage(@PathVariable("prodNo") Integer prodNo, HttpServletResponse response) throws IOException {
        ProductImageVO productImage = prodimgSvc.findPrimaryImageByProdNo(prodNo);
        ServletOutputStream out = response.getOutputStream();

        if (productImage != null && productImage.getProdPic() != null) {
            byte[] imageBytes = productImage.getProdPic();
            response.setContentType("image/jpeg");
            out.write(imageBytes);
        } else {
            // 如果沒有主圖片，顯示默認圖片
            Resource defaultImage = new ClassPathResource("static/img/default.png");
            InputStream inputStream = defaultImage.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] defaultImageBytes = bufferedInputStream.readAllBytes();
            response.setContentType("image/png");
            out.write(defaultImageBytes);
        }
        out.close();
    }



	@GetMapping("/search")
	public String searchProducts(@RequestParam String keyword, Model model) {
	    List<ProductVO> searchResults = storeServiceImpl.searchProducts(keyword);

	  
	    if (searchResults.isEmpty()) {
	        model.addAttribute("message", "沒有找到相關商品。");
	    }

	 
	    model.addAttribute("products", searchResults);

	    return "store";
	}


	@GetMapping("/productdetail/{id}")
	public String productDetail(@PathVariable("id") Integer id, Model model) {
		ProductVO product = storeServiceImpl.getProductById(id);
		
		//取得該商品的占卜師的memId
		FtVO ftVO =product.getFtId();
		String memIdOfFt =String.valueOf(ftVO.getMember().getMemberId());
		model.addAttribute("memIdOfFt",memIdOfFt);
		
		model.addAttribute("product", product);
		return "productdetail";
	}

	// 獲取購物車商品
//    @GetMapping("/{userId}")
//    public List<CartVO> getCartItems(@PathVariable Integer userId) {
//        return cartService.getCartItemsByUserId(userId);
//    }

}
