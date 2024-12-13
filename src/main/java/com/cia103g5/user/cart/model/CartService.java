package com.cia103g5.user.cart.model;

import com.cia103g5.user.cart.model.CartVO;

import java.util.List;

public interface CartService {

	
    void addOrUpdateCartItem(Integer userId, CartVO cartItem);


    List<CartVO> getCartItems(Integer userId);

    
    void removeCartItem(Integer userId, Integer prodNo);

   
    void clearCart(Integer userId);
    
}
