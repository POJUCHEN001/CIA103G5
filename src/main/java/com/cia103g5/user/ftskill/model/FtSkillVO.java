package com.cia103g5.user.ftskill.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.skill.model.SkillVO;

/**
 * 複合表：占卜師個人專長 ft_skill
 */

@Entity
@Table(name = "ft_service")
public class FtSkillVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FtSkillKey id;

	@ManyToOne
	@MapsId("ftId")
	@JoinColumn(name = "ft_id", referencedColumnName = "ft_id")
	private FtVO ftVO;

	@ManyToOne
	@MapsId("skillNo")
	@JoinColumn(name = "skill_no", referencedColumnName = "skill_no")
	private SkillVO skillVO;

	@Column(name = "skill_desc", length = 300)
	@Size(max = 300, message = "專長描述: 300個字以內")
	private String skillDesc;

	@Lob
	@Column(name = "photo", columnDefinition = "LONGBLOB")
	private byte[] photo;

	private static final int MAX_PHOTO_SIZE = 2 * 1024 * 1024;

	public FtSkillVO() {
	}

	public FtSkillKey getId() {
		return id;
	}

	public void setId(FtSkillKey id) {
		this.id = id;
	}

	public FtVO getFtVO() {
		return ftVO;
	}

	public void setFtVO(FtVO ftVO) {
		this.ftVO = ftVO;
	}

	public SkillVO getSkillVO() {
		return skillVO;
	}

	public void setSkillVO(SkillVO skillVO) {
		this.skillVO = skillVO;
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
		return "FtSkillVO [id=" + id + ", ftVO=" + ftVO + ", skillVO=" + skillVO + ", skillDesc=" + skillDesc
				+ ", photo=" + Arrays.toString(photo) + "]";
	}
	
	@Embeddable
	public static class FtSkillKey implements Serializable {
		private static final long serialVersionUID = 1L;

		@Min(value = 1, message = "占卜師編號從1開始")
		@Max(value = 9999, message = "占卜師編號不能大於9999")
		@Column(name = "ft_id")
		private Integer ftId;

		@Min(value = 1, message = "專長編號從1開始")
		@Max(value = 999, message = "專長編號不能大於999")
		@Column(name = "skill_no")
		private Integer skillNo;

		public FtSkillKey() {
		}

		public FtSkillKey(Integer ftId, Integer skillNo) {
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
			FtSkillKey other = (FtSkillKey) obj;
			return Objects.equals(ftId, other.ftId) && Objects.equals(skillNo, other.skillNo);
		}

		@Override
		public String toString() {
			return "FtSkillKey [ftId=" + ftId + ", skillNo=" + skillNo + "]";
		}

	}

}
