package com.cia103g5.user.cart.model;

import java.util.List;

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
	    public List<CartVO> getCartItems(Integer userId) {
	        return cartRepository.findCartByUserId(userId);
	    }

	    @Override
	    public void removeCartItem(Integer userId, Integer prodNo) {
	        cartRepository.deleteCartItem(userId, prodNo);
	    }

	    @Override
	    public void clearCart(Integer userId) {
	        cartRepository.clearCart(userId);
	    }
	
}
