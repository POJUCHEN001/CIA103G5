package com.cia103g5.user.cart.controller;

import com.cia103g5.user.cart.model.CartVO;
import com.cia103g5.user.cart.model.CartServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartServiceImpl cartService;

    @Autowired
    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

 
    @GetMapping("/{userId}")
    public String getCartItems(@PathVariable Integer userId, Model model, HttpSession session) {

        session.setAttribute("redirectUrl", "/cart/" + userId);

        List<CartVO> cartItems = cartService.getCartItems(userId);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("userId", userId);
        return "cart";
    }

 
    @PostMapping("/{userId}")
    public String addOrUpdateCartItem(@PathVariable Integer userId, @ModelAttribute CartVO cartItem, Model model, HttpSession session) {
        try {
        
            session.setAttribute("redirectUrl", "/cart/" + userId);

            cartService.addOrUpdateCartItem(userId, cartItem);
            return "redirect:/cart/" + userId;
        } catch (Exception e) {
            model.addAttribute("error", "無法添加或更新購物車項目：" + e.getMessage());
            return "error";
        }
    }

    
    @PostMapping("/remove/{userId}/{prodNo}")
    public String removeCartItem(@PathVariable Integer userId, @PathVariable Integer prodNo, Model model, HttpSession session) {
        try {
           
            session.setAttribute("redirectUrl", "/cart/" + userId);

            cartService.removeCartItem(userId, prodNo);
            return "redirect:/cart/" + userId;
        } catch (Exception e) {
            model.addAttribute("error", "無法移除購物車項目：" + e.getMessage());
            return "error";
        }
    }

  
    @PostMapping("/clear/{userId}")
    public String clearCart(@PathVariable Integer userId, Model model, HttpSession session) {
        try {
          
            session.setAttribute("redirectUrl", "/cart/" + userId);

            cartService.clearCart(userId);
            return "redirect:/cart/" + userId;
        } catch (Exception e) {
            model.addAttribute("error", "無法清空購物車：" + e.getMessage());
            return "error";
        }
    }

   
    @GetMapping("/redirect")
    public String redirectAfterLogin(HttpSession session) {
        String redirectUrl = (String) session.getAttribute("redirectUrl");
        if (redirectUrl != null) {
            session.removeAttribute("redirectUrl"); // 移除 session 中的 URL
            return "redirect:" + redirectUrl;
        }
        return "redirect:/index"; 
    }
}
