package com.cia103g5.user.cart.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService {

	 private final CartRepositoryImpl cartRepository;

	    @Autowired
	    public CartServiceImpl(CartRepositoryImpl cartRepository) {
	        this.cartRepository = cartRepository;
	    }

	    @Override
	    public void addOrUpdateCartItem(Integer memberId, CartVO cartItem) {
	    	 // 查詢購物車內是否已存在該商品
	        CartVO existingItem = cartRepository.findCartItem(memberId, cartItem.getProdNo());

	        if (existingItem != null) {
	            // 如果有，更新數量並保存
	            Integer updatedQuantity = existingItem.getQuantity() + cartItem.getQuantity();
	            existingItem.setQuantity(updatedQuantity);
	            cartRepository.saveCartItem(memberId, existingItem);
	        } else {
	            // 如果不存在，直接保存新商品
	            cartRepository.saveCartItem(memberId, cartItem);
	        }
	    }

	    @Override
	    public Map<Integer, List<CartVO>> getCartItemsGroupedByFtId(Integer memberId) {
	        List<CartVO> cartItems = cartRepository.findCartBymemberId(memberId);
	        if (cartItems == null || cartItems.isEmpty()) {
	            return new HashMap<>(); // 返回一個空的 Map
	        }

	        return cartItems.stream()
	                        .collect(Collectors.groupingBy(CartVO::getFtId));
	    }
	    
	    
	    @Override
	    public void removeCartItem(Integer memberId, Integer prodNo) {
	        cartRepository.deleteCartItem(memberId, prodNo);
	    }
	    

	    @Override
	    public double calculateTotalAmount(Integer memberId) {
	        List<CartVO> cartItems = cartRepository.findCartBymemberId(memberId);

	  
	        return cartItems.stream()
	                        .mapToDouble(item -> item.getQuantity() * item.getPrice())
	                        .sum();
	    }

	    
	        
	    @Override
	    public void clearCart(Integer memberId) {
	        cartRepository.clearCart(memberId);
	    }
	
	    
}
