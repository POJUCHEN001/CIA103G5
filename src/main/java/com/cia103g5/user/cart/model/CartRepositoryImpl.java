package com.cia103g5.user.cart.model;

import com.cia103g5.user.cart.model.CartVO;
import com.cia103g5.user.cart.model.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CartRepositoryImpl implements CartRepository{
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, CartVO> hashOperations;

    @Autowired
    public CartRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void saveCartItem(Integer userId, CartVO cartItem) {
        String redisKey = "cart:" + userId;
        hashOperations.put(redisKey, String.valueOf(cartItem.getProdNo()), cartItem);
    }

    @Override
    public List<CartVO> findCartByUserId(Integer userId) {
        String redisKey = "cart:" + userId;
        return new ArrayList<>(hashOperations.values(redisKey));
    }

    @Override
    public CartVO findCartItem(Integer userId, Integer prodNo) {
        String redisKey = "cart:" + userId;
        return hashOperations.get(redisKey, String.valueOf(prodNo));
    }

    @Override
    public void deleteCartItem(Integer userId, Integer prodNo) {
        String redisKey = "cart:" + userId;
        hashOperations.delete(redisKey, String.valueOf(prodNo));
    }

    @Override
    public void clearCart(Integer userId) {
        String redisKey = "cart:" + userId;
        redisTemplate.delete(redisKey);
    }
    
}