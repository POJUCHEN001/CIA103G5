//package com.cia103g5.user.chatroom.config;
//
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//import com.cia103g5.user.chatroom.model.RedisService;
//
//public class RedisDataInitializer {
//
//    public static void main(String[] args) {
//        // 使用 RedisConfig 作為上下文配置
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RedisConfig.class);
//
//        // 獲取 RedisService Bean 並執行資料初始化
//        RedisService redisService = context.getBean(RedisService.class);
//        redisService.saveDataToRedis();
//        System.out.println("Redis 資料已成功存入！");
//
//        // 關閉上下文
//        context.close();
//    }
//}
