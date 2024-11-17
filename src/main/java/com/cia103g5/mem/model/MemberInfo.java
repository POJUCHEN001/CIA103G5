package com.cia103g5.mem.model;

import java.io.Serializable;
import java.sql.Date;

import jakarta.persistence.*;

@Entity
@Table(name = "Member_Info")
public class MemberInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 資料庫自增主鍵
	@Column(name = "mem_id")
	private Integer memId;

	@Column(name = "ft_id")
	private Integer ftId;

	@Column(name = "name", nullable = false, length = 50) // 假設名稱必填且最大長度為 50
	private String name;

	@Column(name = "account", nullable = false, unique = true, length = 50) // 帳號唯一且必填
	private String account;

	@Column(name = "password", nullable = false, length = 100) // 密碼加密存儲
	private String password;

	@Column(name = "email", unique = true, length = 100) // email 是唯一
	private String email;

	@Column(name = "email_state")
	private Integer emailState;

	@Column(name = "registered_at")
	private Date registeredAt;

	@Lob // 表示可能是大數據字段
	@Column(name = "photo")
	private byte[] photo;

	@Column(name = "gender")
	private Integer gender;

	@Column(name = "nickname", length = 50)
	private String nickname;

	@Column(name = "status")
	private Integer status;

	@Column(name = "points")
	private Integer points;

	// 無參數構造函數
	public MemberInfo() {
		super();
	}

	// Getter 和 Setter 方法
	public Integer getMemId() {
		return memId;
	}

	public void setMemId(Integer memId) {
		this.memId = memId;
	}

	public Integer getFtId() {
		return ftId;
	}

	public void setFtId(Integer ftId) {
		this.ftId = ftId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEmailState() {
		return emailState;
	}

	public void setEmailState(Integer emailState) {
		this.emailState = emailState;
	}

	public Date getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(Date registeredAt) {
		this.registeredAt = registeredAt;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public Integer getGender() {
		return gender;
	}

	public void setGender(Integer gender) {
		this.gender = gender;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}
}
