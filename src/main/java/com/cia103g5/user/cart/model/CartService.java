package com.cia103g5.user.cart.model;

import java.util.List;
import java.util.Map;

public interface CartService {

	
    void addOrUpdateCartItem(Integer userId, CartVO cartItem);


    Map<Integer, List<CartVO>> getCartItemsGroupedByFtId(Integer userId);

    
    void removeCartItem(Integer userId, Integer prodNo);

   
    void clearCart(Integer userId);
    
    double calculateTotalAmount(Integer userId);
    
}
