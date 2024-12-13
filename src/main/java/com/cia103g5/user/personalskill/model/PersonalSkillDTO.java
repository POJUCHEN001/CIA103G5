package com.cia103g5.user.personalskill.model;

public class PersonalSkillDTO {

	private Integer ftId;
	private String skillName;

	public PersonalSkillDTO(Integer ftId, String skillName) {
		this.ftId = ftId;
		this.skillName = skillName;
	}

	// Getter & Setter
	public Integer getFtId() {
		return ftId;
	}

	public void setFtId(Integer ftId) {
		this.ftId = ftId;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

}
