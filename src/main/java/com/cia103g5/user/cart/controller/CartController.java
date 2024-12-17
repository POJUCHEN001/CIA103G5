package com.cia103g5.user.cart.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cia103g5.user.cart.model.CartServiceImpl;
import com.cia103g5.user.cart.model.CartVO;
import com.cia103g5.user.product.model.ProductService;
import com.cia103g5.user.product.model.ProductVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartServiceImpl cartService;
    private final ProductService productService;

    @Autowired
    public CartController(CartServiceImpl cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }

    // 顯示購物車內容
    @GetMapping("/{memberId}")
    public String getCartItems(@PathVariable Integer memberId, Model model) {
        Map<Integer, List<CartVO>> cartItemsByFtId = cartService.getCartItemsGroupedByFtId(memberId);
        model.addAttribute("cartItemsByFtId", cartItemsByFtId);
        model.addAttribute("memberId", memberId);
        return "cart";
    }

    // 加入購物車
    @PostMapping("/add/{memberId}/{prodNo}")
    public String addToCart(@PathVariable Integer memberId, @PathVariable Integer prodNo,
                            @RequestParam("quantity") Integer quantity, Model model) {
        ProductVO product = productService.getOneProduct(prodNo);

        if (product != null) {
            CartVO cartItem = new CartVO();
            cartItem.setProdNo(product.getProdNo());
            cartItem.setProdName(product.getProdName());
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice());

            cartService.addOrUpdateCartItem(memberId, cartItem);
        }
        return "redirect:/store/products";
    }

    // 直接購買，將商品數據傳遞到結帳頁面
    @PostMapping("/buynow/{memberId}/{prodNo}")
    public String buyNow(@PathVariable Integer memberId, @PathVariable Integer prodNo,
                         @RequestParam("quantity") Integer quantity, Model model, HttpSession session) {
        ProductVO product = productService.getOneProduct(prodNo);

        if (product != null) {
            CartVO cartItem = new CartVO();
            cartItem.setProdNo(product.getProdNo());
            cartItem.setProdName(product.getProdName());
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice());

            session.setAttribute("buyNowItem", cartItem);
            model.addAttribute("buyNowItem", cartItem);
            model.addAttribute("memberId", memberId);
        }
        return "redirect:/checkout/{memberId}";
    }
}
