package com.cia103g5.user.skill.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "ft_skill", uniqueConstraints = { @UniqueConstraint(columnNames = { "skill_name" }) })
public class SkillVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "skill_no")
	private Integer skillNo;

	@NotBlank(message = "專長名稱: 請勿空白")
	@Size(min = 2, max = 20, message = "專長名稱: 長度請在{min}到{max}之間")
	@Column(name = "skill_name", nullable = false, unique = true, length = 20)
	private String skillName;

	public SkillVO() {
	}

	public SkillVO(Integer skillNo, String skillName) {
		this.skillNo = skillNo;
		this.skillName = skillName;
	}

	public Integer getSkillNo() {
		return skillNo;
	}

	public void setSkillNo(Integer skillNo) {
		this.skillNo = skillNo;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(skillNo, skillName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SkillVO other = (SkillVO) obj;
		return Objects.equals(skillNo, other.skillNo) && Objects.equals(skillName, other.skillName);
	}

	@Override
	public String toString() {
		return "SkillVO [skillNo=" + skillNo + ", skillName=" + skillName + "]";
	}

}
