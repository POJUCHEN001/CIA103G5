package com.cia103g5.user.cart.controller;

import java.util.HashMap;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        model.addAttribute("cartItemsByFtId", cartItemsByFtId == null ? new HashMap<>() : cartItemsByFtId);
        model.addAttribute("memberId", memberId);
        return "/cart";
    }

    // 加入購物車
    @PostMapping("/add/{memberId}/{prodNo}")
    public String addToCart(@PathVariable Integer memberId, @PathVariable Integer prodNo,
                            @RequestParam("quantity") Integer quantity, RedirectAttributes redirectAttributes) {
        ProductVO product = productService.getOneProduct(prodNo);

        if (product != null) {
            CartVO cartItem = new CartVO();
            cartItem.setProdNo(product.getProdNo());
            cartItem.setProdName(product.getProdName());
            cartItem.setQuantity(quantity);
            cartItem.setPrice(product.getPrice());
            ProductVO productVO = productService.getOneProduct(prodNo);
            Integer ftId = productVO.getFtId().getFtId();
            String nickname = productVO.getFtId().getNickname();
            System.out.println(nickname);
            cartItem.setFtId(ftId);
            cartItem.setNickname(nickname);

            cartService.addOrUpdateCartItem(memberId, cartItem);
            System.out.println("cartItem: " + cartItem);

            redirectAttributes.addFlashAttribute("successMessage", "成功加入購物車！");
        }
        return "redirect:/store/products";
    }

    // 移除購物車商品，並重新加載購物車頁面
    @GetMapping("/remove/{memberId}/{prodNo}")
    public String removeCartItems(@PathVariable Integer memberId, @PathVariable Integer prodNo, Model model) {
        cartService.removeCartItem(memberId, prodNo);
        Map<Integer, List<CartVO>> cartItemsByFtId = cartService.getCartItemsGroupedByFtId(memberId);
        model.addAttribute("cartItemsByFtId", cartItemsByFtId == null ? new HashMap<>() : cartItemsByFtId);
        model.addAttribute("memberId", memberId);
        return "cart";
    }

    @PostMapping("/decrement/{memberId}/{prodNo}")
    public String decrementCartItem(@PathVariable Integer memberId, @PathVariable Integer prodNo) {
        cartService.decrementCartItem(memberId, prodNo);
        return "redirect:/cart/" + memberId; // 返回購物車頁面
    }

    @PostMapping("/increment/{memberId}/{prodNo}")
    public String incrementCartItem(@PathVariable Integer memberId, @PathVariable Integer prodNo) {
        cartService.incrementCartItem(memberId, prodNo);
        return "redirect:/cart/" + memberId ; // 返回購物車頁面
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
            ProductVO productVO = productService.getOneProduct(prodNo);
            Integer ftId = productVO.getFtId().getFtId();
            String nickname = productVO.getFtId().getNickname();
            cartItem.setFtId(ftId);
            cartItem.setNickname(nickname);


            session.setAttribute("buyNowItem", cartItem);
            model.addAttribute("buyNowItem", cartItem);
            model.addAttribute("memberId", memberId);
        }
        return "redirect:/checkout/{memberId}";
    }
}
