package com.cia103g5.user.chatroom.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cia103g5.user.chatroom.model.ChatMessage;
import com.cia103g5.user.chatroom.model.ChatService;
import com.cia103g5.user.chatroom.model.Member;
import com.cia103g5.user.member.dto.SessionMemberDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpSession;

@Controller
public class ChatController {

	 @Autowired
	 private ChatService chatService;
	 
	 @Autowired
	 private SimpMessagingTemplate messagingTemplate;
	 
	 //接收前端訊息->存儲到redis->回傳原訊息到指定通道
	 @MessageMapping("/chat")
	 public void saveMsg(@Payload ChatMessage message) throws JsonProcessingException {	    
		 	chatService.saveMessage(message);
		 
		 	//指定通道:/custom/發送者:接收者
		 	String destination = "/custom/"+message.getSender()+":"+message.getReceiver();
		  	messagingTemplate.convertAndSend(destination, message);
		  			
	 }
	 
	 
	 //取出歷史訊息
	 @MessageMapping("/history")
	 public void getHistoryMsg(@Payload ChatMessage message) throws JsonMappingException, JsonProcessingException {

//		 		System.out.println("message sender:"+message.getSender());
		 	
		 		//指定通道:/custom/發送者:接收者
			    String destination = "/custom/"+message.getSender()+":"+message.getReceiver();
			    
			    List<ChatMessage> list = chatService.getHistoryMessage(message);	
			    ObjectMapper objectmapper = new ObjectMapper();
			    
			    String historyJson = objectmapper.writeValueAsString(list);

//			    System.out.println("Sending history to: " + destination + " -> " + historyJson);
			    messagingTemplate.convertAndSend(destination, historyJson);//回傳JSON形式的物件
		 	 
	 }
	 
	 
	 //通知占卜師有哪些會員要跟他聊(加入占卜師聊天室的列表當中)
//	 @MessageMapping("/notify")
//	 public void notifyForturnTeller(ChatMessage message) {
//		 String destination ="/user"+message.getReceiver()+"/queue/notifications";//從訊息物件中取出發送者名稱(會員)，傳送給receiver(占卜師)
//		 messagingTemplate.convertAndSend(destination, message.getSender()); //destination為占卜師
//		 
//	 }
	 
	 //將聊天對象加入列表中(預設會員，之後從登錄中抓到)
	 @ResponseBody
	 @PostMapping("/chat/add/friend")
	 public ResponseEntity<String> addNewFriendToList(@RequestParam("ftId") String ftId,HttpSession session){
		//取得登錄的會員id
		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
		Integer memId = sessionMember.getMemberId();
		 
		 chatService.saveFriendList(memId,Integer.valueOf(ftId));//在該會員的聊天列表中加入該占卜師編號
		 
		 return ResponseEntity.ok("新朋友已加入列表");
		 
	 } 
	 
	 
	   //取得自己聊天對象列表的API(回傳list，裡面每個都是字串陣列，存name、id)
		@ResponseBody
		@GetMapping("/chat/list")
		 public ResponseEntity<List<String[]>> getFriendsList(HttpSession session){
			SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
			Integer memId = sessionMember.getMemberId();//從session取得自己的id，用id查到自己所有的聊天對象

			Set<String> list =chatService.getAllFriends(memId);
			Integer sessionFtId = (Integer) session.getAttribute("ftId");
			
			//取出每個id，再做查詢，找到其nickname，將nickname、id一起回傳
			List<String []> nameList = new ArrayList<String[]>();
			if(sessionFtId==0) { //沒有ftId代表為會員->查占卜師
				
				for(String ftid :list) {
					String [] arr =new String[2];
					Member member =chatService.getFtInfo(Integer.valueOf(ftid));
					String ftname =member.getNickname();
					System.out.println("nickname:"+ftname);	
					
					arr[0]=ftname;
					arr[1]=ftid;
					
					nameList.add(arr);			
				}	
			}else { //有ftId代表為占卜師->查會員
				for(String mem :list) {
					String [] arr =new String[2];
					Member member =chatService.getInfo(Integer.valueOf(mem));
					String memname =member.getNickname();
					System.out.println("nickname:"+memname);
					
					arr[0]=memname;
					arr[1]=mem;
					nameList.add(arr);
				}
			}
						
			
			return ResponseEntity.ok(nameList);
		}
	 	 
	 
	 	 
	 
	//取得一個占卜師的資訊(id、nickname、role)
	@ResponseBody
	@GetMapping("/chat/ftinfo")
	public ResponseEntity<?> getFtInfo(@RequestParam("ftId") String ftId) throws JsonProcessingException{
		Integer id =null;
		if(ftId!=null && !ftId.trim().isEmpty()) {
			id =Integer.valueOf(ftId);		
		}
		
		Member member =chatService.getFtInfo(id);
		
		if(member!=null) {
			return ResponseEntity.ok(member);
		}else {
			return ResponseEntity.status(404).body("Fortuneteller with id " + id + " not found.");
		}
	}

	
	//取得一個會員的資訊(id、nickname、role)
	@ResponseBody
	@GetMapping("/chat/meminfo")
	public ResponseEntity<?> getmemInfo(@RequestParam("memId") String memId){
		Integer id =null;
		if(memId!=null && !memId.trim().isEmpty()) {
			id =Integer.valueOf(memId);		
		}
		
		Member member =chatService.getInfo(id);
		
		if(member!=null) {
			return ResponseEntity.ok(member);
		}else {
			return ResponseEntity.status(404).body("Member with id " + id + " not found.");
		}
	} 
	
	//取得登錄會員自己的id和nickname(直接回傳sessionDTO，前端取出來)
	@ResponseBody
	@GetMapping("/chat/getSelf")
	public ResponseEntity<?> getselfInfo(HttpSession session){
		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
		
		if(sessionMember!=null) {
			return ResponseEntity.ok(sessionMember);
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User not logged in");
		}		
		
	}
	
	
	
}
