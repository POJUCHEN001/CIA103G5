package com.cia103g5.user.chatroom.controller;

import java.net.URLEncoder;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Header;
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
	 
	 //接收訊息-進行存儲-回傳原訊息
	 @MessageMapping("/chat")
	 public void saveMsg(Principal principal, @Payload ChatMessage message, @Header("simpSessionAttributes") Map<String, Object> attributes) throws JsonProcessingException {	    
//		  	String nickname = (String) attributes.get("nickname");
//		  	String nickname = "志宏";
		 	chatService.saveMessage(message);
		 
		  	String destination = "/user/" + message.getReceiver() + "/queue/messages";
		  	messagingTemplate.convertAndSend("/user/" + message.getReceiver() + "/queue/messages", message);
//		  	messagingTemplate.convertAndSend(destination, message);
		  	
		  	
//		  	System.out.println("路徑: " + "/user/" + nickname + "/queue/messages");
		  	
//		  	System.out.println("wecksoket session裡面的nickname:"+nickname);
//		  	System.out.println("message sender:"+message.getSender());
//		  	if (!nickname.equals(message.getSender())) {
//		        throw new IllegalStateException("Sender does not match session nickname");
//		    }else {
//		    	//存儲訊息
//			    chatService.saveMessage(message);
//
//			    //destination為接收者的位置
//			    String destination = "/user/" + nickname + "/queue/messages";
//			    System.out.println("Sending message to: " + destination);
//			    
//			    ObjectMapper objectmapper = new ObjectMapper();
//			    String sendJson =objectmapper.writeValueAsString(message);
//			    messagingTemplate.convertAndSend(destination, sendJson);
//			    
////			    messagingTemplate.convertAndSendToUser(
////			            message.getReceiver(),
////			            "/queue/messages",
////			            sendJson
////			        );
//		    }
//		  		
	
	 }
	 
	 
	 //取出歷史訊息
	 @MessageMapping("/history")
	 public void getHistoryMsg(Principal principal ,@Payload ChatMessage message, @Header("simpSessionAttributes") Map<String, Object> attributes) throws JsonMappingException, JsonProcessingException {
//		 	String nickname = (String) attributes.get("nickname");
//		 	String nickname = "Wen";
			 
		 	
//		 	System.out.println("wecksoket session裡面的nickname:"+nickname);
		 	System.out.println("message sender:"+message.getSender());
		 	
//			if (!nickname.equals(message.getSender())) {
//		        throw new IllegalStateException("Sender does not match session nickname");
//		    }else {
		    	//destination為自己
//		    	String encoderName = URLEncoder.encode(principal.getName()).toString();
			    String destination = "/user/" + message.getSender() + "/queue/messages";
			    
			    List<ChatMessage> list = chatService.getHistoryMessage(message);	
			    ObjectMapper objectmapper = new ObjectMapper();
			    
			    String historyJson = objectmapper.writeValueAsString(list);

			    System.out.println("Sending history to: " + destination + " -> " + historyJson);
			    messagingTemplate.convertAndSend(destination, historyJson);
			    

		 	 
	 }
	 
	 
	 //通知占卜師有哪些會員要跟他聊(加入占卜師聊天室的列表當中)
	 @MessageMapping("/notify")
	 public void notifyForturnTeller(ChatMessage message) {
		 String destination ="/user"+message.getReceiver()+"/queue/notifications";//從訊息物件中取出發送者名稱(會員)，傳送給receiver(占卜師)
		 messagingTemplate.convertAndSend(destination, message.getSender()); //destination為占卜師
		 
	 }
	 
	 //將聊天對象加入列表中(預設會員，之後從登錄中抓到)
	 @ResponseBody
	 @PostMapping("/chat/add/friend")
	 public ResponseEntity<String> addNewFriendToList(@RequestParam("ftId") String ftId,Principal principal,HttpSession session){
		//取得登錄的會員id
		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
		Integer memId = sessionMember.getMemberId();
		 
		 
//		 Integer id =1;
		 chatService.saveFriendList(memId,Integer.valueOf(ftId));//在該會員的聊天列表中加入該占卜師編號
		 
		 return ResponseEntity.ok("新朋友已加入列表");
		 
	 } 
	 
	 
	 //一個人取得自己的聊天列表對象
	@ResponseBody
	@GetMapping("/chat/list")
	 public ResponseEntity<List<String>> getFriendsList(Principal principal,HttpSession session){
		SessionMemberDTO sessionMember = (SessionMemberDTO)session.getAttribute("loggedInMember");
		Integer memId = sessionMember.getMemberId();
//		Integer id =1;
		Set<String> list =chatService.getAllFriends(memId);
		Integer sessionFtId = (Integer) session.getAttribute("ftId");
		
		//取出每個id，再做查詢，找到其nickname，回傳nickname的list
		List<String> nameList = new ArrayList<String>();
		if(sessionFtId==null) { //沒有ftId代表為會員->查占卜師
			
			for(String ftid :list) {
				Member member =chatService.getFtInfo(Integer.valueOf(ftid));
				String ftname =member.getNickname();
				System.out.println("nickname:"+ftname);
				nameList.add(ftname);			
			}	
		}else { //有ftId代表為占卜師->查會員
			for(String mem :list) {
				Member member =chatService.getInfo(Integer.valueOf(mem));
				String memname =member.getNickname();
				System.out.println("nickname:"+memname);
				nameList.add(memname);
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
//		ObjectMapper objectMapper =new ObjectMapper();
//		String json =objectMapper.writeValueAsString(member);
		
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
