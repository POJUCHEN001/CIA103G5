package com.cia103g5.user.chatroom.model;

//用來存放一個聊天室的資訊，轉成JSON存入redis
public class ChatMessage {
	private String type;
	private String sender;
	private String receiver;
	private String message;
	private String sendTime;
	
	public ChatMessage() {
		super();
	}


	public ChatMessage(String type, String sender, String receiver, String message, String sendTime) {
		super();
		this.type = type;
		this.sender = sender;
		this.receiver = receiver;
		this.message = message;
		this.sendTime = sendTime;
	}



	public String getType() {
		return type;
	}



	public void setType(String type) {
		this.type = type;
	}



	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	
	
	
}
