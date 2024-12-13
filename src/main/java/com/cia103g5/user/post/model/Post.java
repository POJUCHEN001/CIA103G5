package com.cia103g5.user.post.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "post")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_no")
    private Integer postNo;

    @Column(name = "user_type", nullable = false)
    private Byte userType;

    @Column(name = "mem_id", nullable = false)
    private Integer memId;

    @ManyToOne
    @JoinColumn(name = "category_no", nullable = false)
    private PostCategory postCategory;

    @Column(name = "title", length = 200, nullable = false)
    private String title;

    @Column(name = "author_name", length = 50, nullable = false)
    private String authorName;

    @Column(name = "post_time", nullable = false)
    private LocalDateTime postTime;

    @Column(name = "mod_time", nullable = false)
    private LocalDateTime modTime;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "view", columnDefinition = "INT DEFAULT 0")
    private Integer view;

    @Column(name = "reply", columnDefinition = "INT DEFAULT 0")
    private Integer reply;

    @Column(name = "status", nullable = false)
    private Byte status;

    @Column(name = "total_likes", columnDefinition = "INT DEFAULT 0")
    private Integer totalLikes;

    @Column(name = "total_dislikes", columnDefinition = "INT DEFAULT 0")
    private Integer totalDislikes;
    

    @Transient
    private String formattedPostTime;
    
    

    // Getters and Setters
    public Integer getPostNo() {
        return postNo;
    }

    public void setPostNo(Integer postNo) {
        this.postNo = postNo;
    }

	public Byte getUserType() {
		return userType;
	}

	public void setUserType(Byte userType) {
		this.userType = userType;
	}

	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public PostCategory getPostCategory() {
		return postCategory;
	}

	public void setPostCategory(PostCategory postCategory) {
		this.postCategory = postCategory;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthorName() {
		return authorName;
	}

	public void setAuthorName(String authorName) {
		this.authorName = authorName;
	}

	public LocalDateTime getPostTime() {
		return postTime;
	}

	public void setPostTime(LocalDateTime postTime) {
		this.postTime = postTime;
	}

	public LocalDateTime getModTime() {
		return modTime;
	}

	public void setModTime(LocalDateTime modTime) {
		this.modTime = modTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getView() {
		return view;
	}

	public void setView(Integer view) {
		this.view = view;
	}

	public Integer getReply() {
		return reply;
	}

	public void setReply(Integer reply) {
		this.reply = reply;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Integer getTotalLikes() {
		return totalLikes;
	}

	public void setTotalLikes(Integer totalLikes) {
		this.totalLikes = totalLikes;
	}

	public Integer getTotalDislikes() {
		return totalDislikes;
	}

	public void setTotalDislikes(Integer totalDislikes) {
		this.totalDislikes = totalDislikes;
	}

	public String getFormattedPostTime() {
		return formattedPostTime;
	}

	public void setFormattedPostTime(String formattedPostTime) {
		this.formattedPostTime = formattedPostTime;
	}

    // 其他 Getters 和 Setters
}
