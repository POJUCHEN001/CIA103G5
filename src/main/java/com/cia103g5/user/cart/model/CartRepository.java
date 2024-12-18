package com.cia103g5.user.cart.model;

import java.util.List;

public interface CartRepository {
	
    void saveCartItem(Integer memberId, CartVO cartItem);
    
    List<CartVO> findCartBymemberId(Integer memberId);
    
    CartVO findCartItem(Integer memberId, Integer prodNo);
    
    void deleteCartItem(Integer memberId, Integer prodNo);
    
    void updateCart(Integer memberId, List<CartVO> cartItems);
    
    void clearCart(Integer memberId);
}
