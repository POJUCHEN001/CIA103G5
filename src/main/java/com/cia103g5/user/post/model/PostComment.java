package com.cia103g5.user.post.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "post_comment") // 對應資料庫中的 post_comment 表格
public class PostComment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 主鍵自動生成
	private int commentNo; // 留言編號 (主鍵)

	@Column(name = "post_no", nullable = false) // 關聯到文章編號
	private int postNo; // 發文編號

	@Column(name = "mem_id", nullable = false) // 關聯到會員編號
	private int memId; // 發文編號

	@Column(name = "comment_text")
	private String commentText; // 留言內容

	@Column(name = "created_time", nullable = false)
	private LocalDateTime createdTime;

	@Column(name = "updated_time")
	private LocalDateTime updatedTime;

//	private String memNickname;
//
//	public String getMemNickname() {
//		return memNickname;
//	}
//
//	public void setMemNickname(String memNickname) {
//		this.memNickname = memNickname;
//	}

	// Getters 和 Setters
	public int getCommentNo() {
		return commentNo;
	}

	public void setCommentNo(int commentNo) {
		this.commentNo = commentNo;
	}

	public int getPostNo() {
		return postNo;
	}

	public void setPostNo(int postNo) {
		this.postNo = postNo;
	}

	public int getMemId() {
		return memId;
	}

	public void setMemId(int memId) {
		this.memId = memId;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public LocalDateTime getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(LocalDateTime updatedTime) {
		this.updatedTime = updatedTime;
	}
}
