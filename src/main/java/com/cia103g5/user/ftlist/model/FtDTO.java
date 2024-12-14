package com.cia103g5.user.ftlist.model;

import java.util.Base64;
import java.util.List;

import com.cia103g5.user.ftgrade.model.FtGrade;

public class FtDTO {
	private Integer ftId;
	private String companyName;
	private String nickname;
	private byte[] photo;
	private String photoBase64; // Base64 照片
	private String intro;
	private Integer price;
	private FtGrade ftRank;
	private List<String> skillNames;
	private boolean isFavorite;

	public FtDTO(Integer ftId, String companyName, String nickname, byte[] photo, String intro, Integer price, List<String> skillNames,
			FtGrade ftRank, boolean isFavorite) {
		this.ftId = ftId;
		this.companyName = companyName;
		this.nickname = nickname;
		this.photo = photo;
		if (photo != null) {
			this.photoBase64 = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo);
		}
		this.intro = intro;
		this.price = price;
		this.skillNames = skillNames;
		this.ftRank = ftRank;
		this.isFavorite = isFavorite;
	}

	// getter 和 setter
	public Integer getFtId() {
		return ftId;
	}

	public void setFtId(Integer ftId) {
		this.ftId = ftId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getPhotoBase64() {
		if (photo != null) {
			return "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(photo);
		}
		return null;
	}

	public void setPhotoBase64(String photoBase64) {
		this.photoBase64 = photoBase64;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public FtGrade getFtRank() {
		return ftRank;
	}

	public void setFtRank(FtGrade ftRank) {
		this.ftRank = ftRank;
	}

	public List<String> getSkillNames() {
		return skillNames;
	}

	public void setSkillNames(List<String> skillNames) {
		this.skillNames = skillNames;
	}

	public boolean getIsFavorite() {
		return isFavorite;
	}

	public void setIsFavorite(boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	
}
