package com.cia103g5.user.ftservice.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import com.cia103g5.user.ftinfo.model.FtInfo;
import com.cia103g5.user.ftskill.model.FtSkillVO;

// (ft_service) VO

@Entity
@Table(name = "ft_service") // 表格名稱
public class FtServiceVO implements Serializable {
	private static final long serialVersionUID = 1L;

	private FtServicePK pk; // 複合主鍵
	private String skillDesc;
	private byte[] photo;
	private FtInfo ftInfo;		// 外來鍵關聯到 ft_info
	private FtSkillVO ftSkill;	// 外來鍵關聯到 ft_skill

	public FtServiceVO() {
	}

	@EmbeddedId
	public FtServicePK getPk() {
		return pk;
	}

	public void setPk(FtServicePK pk) {
		this.pk = pk;
	}

	@Column(name = "skill_desc", length = 1000)
	@Size(max = 1000, message = "專長描述: 最多不超過{max}個字元")
	public String getSkillDesc() {
		return skillDesc;
	}

	public void setSkillDesc(String skillDesc) {
		this.skillDesc = skillDesc;
	}

	@Column(name = "photo")
	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	@ManyToOne
	@JoinColumn(name = "ft_id", insertable = false, updatable = false) // 外來鍵 ft_id
	public FtInfo getFtInfo() {
		return ftInfo;
	}

	public void setFtInfo(FtInfo ftInfo) {
		this.ftInfo = ftInfo;
	}

	@ManyToOne
	@JoinColumn(name = "skill_no", insertable = false, updatable = false) // 外來鍵 skill_no
	public FtSkillVO getFtSkill() {
		return ftSkill;
	}

	public void setFtSkill(FtSkillVO ftSkill) {
		this.ftSkill = ftSkill;
	}
}

@Embeddable
class FtServicePK implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer ftId;	 // 主鍵一部分
	private Integer skillNo; // 主鍵一部分

	public FtServicePK() {
	}

	public FtServicePK(Integer ftId, Integer skillNo) {
		this.ftId = ftId;
		this.skillNo = skillNo;
	}

	@Column(name = "ft_id")
	public Integer getFtId() {
		return ftId;
	}

	public void setFtId(Integer ftId) {
		this.ftId = ftId;
	}

	@Column(name = "skill_no")
	public Integer getSkillNo() {
		return skillNo;
	}

	public void setSkillNo(Integer skillNo) {
		this.skillNo = skillNo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ftId, skillNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FtServicePK other = (FtServicePK) obj;
		return Objects.equals(ftId, other.ftId) && Objects.equals(skillNo, other.skillNo);
	}

}
