package com.cia103g5.user.chatroom.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 儲存會員與占卜師資料到 Redis
    public void saveDataToRedis() {
        // 資料模擬
        Object[][] data = {
                {"1", "Wen", "會員"},
                {"2", "心怡", "會員"},
                {"3", "詩婷", "會員"},
                {"4", "傑克", "會員"},
                {"5", "美琪", "會員"},
                {"6", "志宏", "占卜師"},
                {"7", "小雅", "占卜師"},
                {"8", "家榮", "占卜師"},
                {"9", "YiAnn", "占卜師"},
                {"10", "TingWei", "占卜師"}
        };

        for (Object[] record : data) {
            String id = (String) record[0];
            String nickname = (String) record[1];
            String role = (String) record[2];

            // 建立 Hash 結構
            Map<String, String> hashData = new HashMap<>();
            hashData.put("id", id);
            hashData.put("nickname", nickname);
            hashData.put("role", role);

            // 決定儲存的 Redis 鍵
            String redisKey = role.equals("會員") ? "member:" + id : "fortuneteller:" + id;

            // 儲存到 Redis
            redisTemplate.opsForHash().putAll(redisKey, hashData);
            System.out.println("key"+redisKey);
            System.out.println("hashData"+hashData);
        }

        System.out.println("ID、暱稱與角色資料已成功存入 Redis！");
    }

	public RedisService() {
		super();

	}

	public RedisService(RedisTemplate<String, Object> redisTemplate) {
		super();
		this.redisTemplate = redisTemplate;
	}


    
    
    
}