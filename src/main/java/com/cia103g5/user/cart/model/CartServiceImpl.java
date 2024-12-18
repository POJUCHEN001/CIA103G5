package com.cia103g5.user.cart.model;

import java.util.ArrayList;
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
	        // 從 Redis 取得數據
	        List<CartVO> cartItems = new ArrayList<>();
	        Object redisData = cartRepository.findCartBymemberId(memberId);

	        if (redisData == null) {
	            return new HashMap<>(); // 返回空的 Map
	        }

	        // 檢查數據類型並處理
	        if (redisData instanceof CartVO) {
	            cartItems.add((CartVO) redisData);
	        } else if (redisData instanceof List) {
	            cartItems = (List<CartVO>) redisData;
	        }

	        // 根據 ftId 分組
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
	    public void decrementCartItem(Integer memberId, Integer prodNo) {
	        // 從 Redis 取得購物車數據
	        List<CartVO> cartItems = cartRepository.findCartBymemberId(memberId);

	        if (cartItems != null) {
	            // 查找指定商品
	            for (CartVO item : cartItems) {
	                if (item.getProdNo().equals(prodNo)) {
	                    // 減少數量，但不低於 1
	                    int newQuantity = item.getQuantity() - 1;
	                    if (newQuantity > 0) {
	                        item.setQuantity(newQuantity);
	                    } else {
	                        // 如果數量小於等於 0，則移除該商品
	                        cartItems.remove(item);
	                    }
	                    break;
	                }
	            }

	            // 更新購物車數據回 Redis
	            cartRepository.updateCart(memberId, cartItems);
	        }
	    }
	    

	    
	    
	    @Override
	    public void incrementCartItem(Integer memberId, Integer prodNo) {
	        // 從 Redis 取得購物車數據
	        List<CartVO> cartItems = cartRepository.findCartBymemberId(memberId);

	        if (cartItems != null) {
	            // 查找指定商品
	            for (CartVO item : cartItems) {
	                if (item.getProdNo().equals(prodNo)) {
	                    // 增加數量
	                    item.setQuantity(item.getQuantity() + 1);
	                    break;
	                }
	            }

	            // 更新購物車數據回 Redis
	            cartRepository.updateCart(memberId, cartItems);
	        }
	    }
	    
	        
	    @Override
	    public void clearCart(Integer memberId) {
	        cartRepository.clearCart(memberId);
	    }
	
	    
}
