package com.cia103g5.user.chatroom.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChatService {

	@Autowired
	private RedisTemplate<String,String> redisTemplate;
	
	
	//存入訊息的方法
	public boolean saveMessage(ChatMessage message) throws JsonProcessingException {
		 LocalDateTime now = LocalDateTime.now();

        // 格式化為指定的格式（如：yyyy-MM-dd HH:mm）
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = now.format(formatter);
		message.setSendTime(formattedDateTime);
		
		ObjectMapper objectMapper =new ObjectMapper();
		String json =objectMapper.writeValueAsString(message);
		
		String senderKey = message.getSender()+":"+message.getReceiver();
		String receiverKey =message.getReceiver()+":"+message.getSender();
		
		
		ListOperations<String, String> ops =redisTemplate.opsForList();
		Long beforeSize =ops.size(senderKey);
		
		Long afterSize = ops.rightPush(senderKey, json);
		ops.rightPush(receiverKey, json);
		
		
		if(afterSize>beforeSize) {
			System.out.println("已成功將訊息存在redis，請到redis查看");
			return true;
		}else {
			System.out.println("訊息存儲失敗QQ");
			return false;
		}
		
	}
	
	
	//取出歷史紀錄的方法
	public List<ChatMessage> getHistoryMessage(ChatMessage message) throws JsonMappingException, JsonProcessingException{
		String senderKey = message.getSender()+":"+message.getReceiver();
		ListOperations<String, String> ops =redisTemplate.opsForList();
		ObjectMapper objectMapper =new ObjectMapper();
		
		 List<String> historyMsg =ops.range(senderKey,0,-1);
		 List<ChatMessage> objList =new ArrayList<ChatMessage>();
		 
		 for(String onemsg:historyMsg) {
			 ChatMessage chatobj =objectMapper.readValue(onemsg,ChatMessage.class);
			 chatobj.setType("history");
			 objList.add(chatobj);
		 }
		 
		
		return objList;	
		
	}
	
	
	//取得會員的資訊->id、role、nickname
	public Member getInfo(Integer id) {
		String key ="member:"+id;
		HashOperations<String, String, String> hop =redisTemplate.opsForHash();//key,attribute,value
		String nickname =hop.get(key,"nickname");
		String role =hop.get(key,"role");
		
		Member member =new Member();
		member.setId(id);
		member.setNickname(nickname);
		member.setRole(role);
		
		return member;
	}
	
	
	//取得占卜師的資訊->id、role、nickname
	public Member getFtInfo(Integer id) {
		String key ="fortuneteller:"+id;
		HashOperations<String, String, String> hop =redisTemplate.opsForHash();//key,attribute,value
		String nickname =hop.get(key,"nickname");
		String role =hop.get(key,"role");
		
		Member member =new Member();
		member.setId(id);
		member.setNickname(nickname);
		member.setRole(role);
		
		return member;
	}
	
	//存入新加入列表的聊天對象(會員加入時對象時，同時也在占卜師這裡加入)-->修改邏輯:加入對象時，同時也存入nickname
	public void saveFriendList(Integer memId,Integer ftId) {
		  if (memId == null || ftId == null) {
		        throw new IllegalArgumentException("memId 和 ftId 不能為 null");
		    }
		String memkey ="friends:"+memId;
		String ftkey ="friends:"+ftId;
		
		SetOperations<String,String> setop = redisTemplate.opsForSet();//-->用set存(不可重複)
		
		setop.add(memkey, String.valueOf(ftId));
		setop.add(ftkey, String.valueOf(memId));
				
	}
	
	
	
	//取出一個人所有的聊天對象  -->修改邏輯:取出所有的id 和 nickname
	public Set<String> getAllFriends(Integer id){
		if (id==null) {
	        throw new IllegalArgumentException("id 不能為 null");
	    }
		SetOperations<String,String> setop = redisTemplate.opsForSet();
		
		String key ="friends:"+id;
		return setop.members(key);	
		
	}
	
	
	
}
