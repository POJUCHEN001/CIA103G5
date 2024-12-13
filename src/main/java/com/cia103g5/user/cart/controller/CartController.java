package com.cia103g5.user.cart.controller;

import com.cia103g5.user.cart.model.CartVO;
import com.cia103g5.user.cart.model.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	 private final CartService cartService;

	    @Autowired
	    public CartController(CartService cartService) {
	        this.cartService = cartService;
	    }

	   
	    @PostMapping("/{userId}")
	    public void addOrUpdateCartItem(@PathVariable Integer userId, @RequestBody CartVO cartItem) {
	        cartService.addOrUpdateCartItem(userId, cartItem);
	    }

	  
	    @GetMapping("/{userId}")
	    public List<CartVO> getCartItems(@PathVariable Integer userId) {
	        return cartService.getCartItems(userId);
	    }

	   
	    @DeleteMapping("/{userId}/{prodNo}")
	    public void removeCartItem(@PathVariable Integer userId, @PathVariable Integer prodNo) {
	        cartService.removeCartItem(userId, prodNo);
	    }

	   
	    @DeleteMapping("/{userId}")
	    public void clearCart(@PathVariable Integer userId) {
	        cartService.clearCart(userId);
	    }
}
