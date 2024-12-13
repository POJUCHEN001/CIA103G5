package com.cia103g5.user.cart.model;

import com.cia103g5.user.cart.model.CartVO;
import java.util.List;

public interface CartRepository {
	
    void saveCartItem(Integer userId, CartVO cartItem);

    
    List<CartVO> findCartByUserId(Integer userId);

    
    CartVO findCartItem(Integer userId, Integer prodNo);

   
    void deleteCartItem(Integer userId, Integer prodNo);

   
    void clearCart(Integer userId);
}
