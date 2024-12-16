package com.cia103g5.user.cart.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

	 private final CartRepository cartRepository;

	    @Autowired
	    public CartServiceImpl(CartRepository cartRepository) {
	        this.cartRepository = cartRepository;
	    }

	    @Override
	    public void addOrUpdateCartItem(Integer userId, CartVO cartItem) {
	        cartRepository.saveCartItem(userId, cartItem);
	    }

	    @Override
	    public Map<Integer, List<CartVO>> getCartItemsGroupedByFtId(Integer userId) {
	        List<CartVO> cartItems = cartRepository.findCartByUserId(userId);
	        if (cartItems == null || cartItems.isEmpty()) {
	            return new HashMap<>(); // 返回一個空的 Map
	        }

	        return cartItems.stream()
	                        .collect(Collectors.groupingBy(CartVO::getFtId));
	    }
	    
	    
	    @Override
	    public void removeCartItem(Integer userId, Integer prodNo) {
	        cartRepository.deleteCartItem(userId, prodNo);
	    }
	    

	    @Override
	    public double calculateTotalAmount(Integer userId) {
	        List<CartVO> cartItems = cartRepository.findCartByUserId(userId);

	  
	        return cartItems.stream()
	                        .mapToDouble(item -> item.getQuantity() * item.getPrice())
	                        .sum();
	    }

	    
	        
	    @Override
	    public void clearCart(Integer userId) {
	        cartRepository.clearCart(userId);
	    }
	
	    
}
