package com.cia103g5.user.personalskill.model;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.ftskill.model.FtSkillVO;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * 複合表：占卜師個人專長 personal_skill
 */

@Entity
@Table(name = "ft_service")
public class PersonalSkillVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private PersonalSkillKey id;

	@ManyToOne
	@MapsId("ftId")
	@JoinColumn(name = "ft_id", referencedColumnName = "ft_id")
	private FtVO ftVO;

	@ManyToOne
	@MapsId("skillNo")
	@JoinColumn(name = "skill_no", referencedColumnName = "skill_no")
	private FtSkillVO ftSkillVO;

	@Column(name = "skill_desc", length = 300)
	@Size(max = 300, message = "專長描述: 300個字以內")
	private String skillDesc;

	@Lob
	@Column(name = "photo", columnDefinition = "LONGBLOB")
	private byte[] photo;

	private static final int MAX_PHOTO_SIZE = 2 * 1024 * 1024;

	public PersonalSkillVO() {
	}

	public PersonalSkillKey getId() {
		return id;
	}

	public void setId(PersonalSkillKey id) {
		this.id = id;
	}

	public FtVO getFtVO() {
		return ftVO;
	}

	public void setFtVO(FtVO ftVO) {
		this.ftVO = ftVO;
	}

	public FtSkillVO getFtSkillVO() {
		return ftSkillVO;
	}

	public void setSkillVO(FtSkillVO ftSkillVO) {
		this.ftSkillVO = ftSkillVO;
	}

	public String getSkillDesc() {
		return skillDesc;
	}

	public void setSkillDesc(String skillDesc) {
		this.skillDesc = skillDesc;
	}

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		if (photo != null && photo.length > MAX_PHOTO_SIZE) {
			throw new IllegalArgumentException("照片大小超過" + (MAX_PHOTO_SIZE / (1024 * 1024)) + "MB，請壓縮後再上傳");
		}
		this.photo = photo;
	}

	@Override
	public String toString() {
		return "PersonalSkillVO [id=" + id + ", ftVO=" + ftVO + ", ftSkillVO=" + ftSkillVO + ", skillDesc=" + skillDesc
				+ ", photo=" + Arrays.toString(photo) + "]";
	}

	@Embeddable
	public static class PersonalSkillKey implements Serializable {
		private static final long serialVersionUID = 1L;

		@Min(value = 1, message = "占卜師編號從1開始")
		@Max(value = 9999, message = "占卜師編號不能大於9999")
		@Column(name = "ft_id")
		private Integer ftId;

		@Min(value = 1, message = "專長編號從1開始")
		@Max(value = 999, message = "專長編號不能大於999")
		@Column(name = "skill_no")
		private Integer skillNo;

		public PersonalSkillKey() {
		}

		public PersonalSkillKey(Integer ftId, Integer skillNo) {
			this.ftId = ftId;
			this.skillNo = skillNo;
		}

		public Integer getFtId() {
			return ftId;
		}

		public void setFtId(Integer ftId) {
			this.ftId = ftId;
		}

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
			PersonalSkillKey other = (PersonalSkillKey) obj;
			return Objects.equals(ftId, other.ftId) && Objects.equals(skillNo, other.skillNo);
		}

		@Override
		public String toString() {
			return "PersonalSkillKey [ftId=" + ftId + ", skillNo=" + skillNo + "]";
		}

	}

}
