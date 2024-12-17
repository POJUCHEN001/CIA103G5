package com.cia103g5.common.verificationcode;

import java.util.concurrent.TimeUnit;
/**#########################
#                          #
#     驗證碼產生器service     # 
#                          #
##########################*/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class VerificationCodeService {

	// 自動注入的 RedisTemplate，用於操作 Redis。
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	// 驗證碼在 Redis 中的過期時間（單位：分鐘）
	private static final long CODE_EXPIRE_TIME = 3;

	// Redis 中驗證碼鍵的前綴，用於區分不同應用的資料。
	private static final String REDIS_KEY_PREFIX = "app_name:auth:verification:code:";

	// 用電子郵件，生成 Redis 的 Key, return Key
	private String generateRedisKey(String email) {
		return REDIS_KEY_PREFIX + email;
	}

	// 根據Key 將驗證碼存入 Redis
	public void saveVerificationCodeToRedis(String email, String code) {
		String redisKey = generateRedisKey(email);
		try {
			// 使用 opsForValue().set() 方法將資料存入 Redis 並設定過期時間。
			redisTemplate.opsForValue().set(redisKey, code, CODE_EXPIRE_TIME, TimeUnit.MINUTES);
			System.out.println("Verification code saved to Redis for email: " + email);
		} catch (Exception e) {
			System.err.println("Failed to save verification code to Redis for email: " + email);
			e.printStackTrace();
		}
	}

	// 根據用戶的 mail 從 Redis 中獲取驗證碼
	public String getVerificationCodeFromRedis(String email) {
		
		String redisKey = "app_name:auth:verification:code:" + email;
//		System.out.println("取得Redis Key: " + redisKey);
		try {
//			System.out.println("取得Redis: " + redisTemplate.opsForValue().get(redisKey));
			return redisTemplate.opsForValue().get(redisKey);
			
		} catch (Exception e) {
			System.err.println("Failed to retrieve verification code from Redis for email: " + email);
			e.printStackTrace();
			return null;
		}
	}

	// 根據用戶 mail 刪除 Redis 中的驗證碼
	public void deleteVerificationCodeFromRedis(String email) {
		String redisKey = "app_name:auth:verification:code:" + email;
		redisTemplate.delete(redisKey);
		System.out.println("Verification code deleted from Redis for email: " + email);
	}

}
