package com.cia103g5.user.member.model;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class MemberPasswordTokenService {
	
	@Autowired
    private StringRedisTemplate redisTemplate;
	
	// 存儲 key-value 並設置過期時間
	public void set(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }
	
	// 取得指定 key 的值
	public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }
	
	// 刪除指定的key
	public void delete(String key) {
        redisTemplate.delete(key);
    }

}
