package com.cia103g5.user.ftskill.model;

import java.util.Objects;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.skill.model.SkillVO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "ft_service")
@IdClass(FtSkillVO.FtSkillId.class) // 使用內部類作為組合鍵
public class FtSkillVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private FtVO ftId;
    private SkillVO skillNo;
    private String skillDesc;
    private byte[] photo;

    public FtSkillVO() {
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "ft_id", nullable = false)
    @NotNull(message = "占卜師編號: 請勿空白")
    public FtVO getFtId() {
        return ftId;
    }

    public void setFtId(FtVO ftId) {
        this.ftId = ftId;
    }

    @Id
    @ManyToOne
    @JoinColumn(name = "skill_no", nullable = false)
    @NotNull(message = "專長編號: 請勿空白")
    public SkillVO getSkillNo() {
        return skillNo;
    }

    public void setSkillNo(SkillVO skillNo) {
        this.skillNo = skillNo;
    }

    @Column(name = "skill_desc", length = 300)
    @Size(max = 300, message = "專長描述: 300個字以內")
    public String getSkillDesc() {
        return skillDesc;
    }

    public void setSkillDesc(String skillDesc) {
        this.skillDesc = skillDesc;
    }

    @Lob
    @Column(name = "photo")
    public byte[] getPhoto() {
        return photo;
    }

    private static final int MAX_PHOTO_SIZE = 2 * 1024 * 1024;

    public void setPhoto(byte[] photo) {
        if (photo != null && photo.length > MAX_PHOTO_SIZE) {
            throw new IllegalArgumentException("照片大小超過" + (MAX_PHOTO_SIZE / (1024 * 1024)) + "MB，請壓縮後再上傳");
        }
        this.photo = photo;
    }

    @Override
    public String toString() {
        return "FtSkillVO [ftId=" + ftId + ", skillNo=" + skillNo + ", skillDesc=" + skillDesc +
        		", photo=" + (photo != null ? ("存在圖片, 大小=" + photo.length + " bytes") : "無圖片") + "]";
    }

    /**
     * 靜態內部類：複合主鍵類
     */
    public static class FtSkillId implements java.io.Serializable {
        private static final long serialVersionUID = 1L;

        private Integer ftId;
        private Integer skillNo;

        public FtSkillId() {
        }

        public FtSkillId(Integer ftId, Integer skillNo) {
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
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            FtSkillId other = (FtSkillId) obj;
            return Objects.equals(ftId, other.ftId) && Objects.equals(skillNo, other.skillNo);
        }
    }
}