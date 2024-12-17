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
    public void saveCartItem(Integer memberId, CartVO cartItem) {
        String redisKey = "cart:" + memberId;
        hashOperations.put(redisKey, String.valueOf(cartItem.getProdNo()), cartItem);
    }

    @Override
    public List<CartVO> findCartBymemberId(Integer memberId) {
        String redisKey = "cart:" + memberId;
        return new ArrayList<>(hashOperations.values(redisKey));
    }

    @Override
    public CartVO findCartItem(Integer memberId, Integer prodNo) {
        String redisKey = "cart:" + memberId;
        return hashOperations.get(redisKey, String.valueOf(prodNo));
    }

    @Override
    public void deleteCartItem(Integer memberId, Integer prodNo) {
        String redisKey = "cart:" + memberId;
        hashOperations.delete(redisKey, String.valueOf(prodNo));
    }

    @Override
    public void clearCart(Integer memberId) {
        String redisKey = "cart:" + memberId;
        redisTemplate.delete(redisKey);
    }
    
}