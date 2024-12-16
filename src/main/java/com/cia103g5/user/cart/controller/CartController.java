package com.cia103g5.user.cart.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.cia103g5.user.cart.model.CartServiceImpl;
import com.cia103g5.user.cart.model.CartVO;
import com.cia103g5.user.member.model.MemberService;
import com.cia103g5.user.member.model.MemberVO;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartServiceImpl cartService;
    
    @Autowired
    private MemberService memberService;
    
    @Autowired
    public CartController(CartServiceImpl cartService) {
        this.cartService = cartService;
    }

    // 顯示購物車內容，並根據 ftId 分組
    @GetMapping("/{userId}")
    public String getCartItems(@PathVariable Integer userId, Model model, HttpSession session) {
        session.setAttribute("redirectUrl", "/cart/" + userId);

        // 使用分組方法將商品按 ftId 分組
        Map<Integer, List<CartVO>> cartItemsByFtId = cartService.getCartItemsGroupedByFtId(userId);

        model.addAttribute("cartItemsByFtId", cartItemsByFtId);
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
    
}
