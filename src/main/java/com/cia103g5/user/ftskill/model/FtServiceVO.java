package com.cia103g5.user.ftskill.model;

import java.io.Serializable;
import java.util.Objects;

import com.cia103g5.user.ft.model.FtVO;

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
@IdClass(FtServiceVO.FtSkillId.class) // 使用內部類作為組合鍵
public class FtServiceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "ft_id", nullable = false)
    @NotNull(message = "占卜師編號: 請勿空白")
    private FtVO ftId;

    @Id
    @Column(name = "skill_no", nullable = false)
    @NotNull(message = "專長編號: 請勿空白")
    private Integer skillNo; // 与 FtSkillId 中的字段名和类型一致

    @ManyToOne
    @JoinColumn(name = "skill_no", insertable = false, updatable = false)
    private FtSkillVO ftSkillVO; // 用于关联的实体，不属于主键

    @Column(name = "skill_desc", length = 300)
    @Size(max = 300, message = "專長描述: 300個字以內")
    private String skillDesc;

    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

    private static final int MAX_PHOTO_SIZE = 2 * 1024 * 1024;

    public FtServiceVO() {
    }

    public FtVO getFtId() {
        return ftId;
    }

    public void setFtId(FtVO ftId) {
        this.ftId = ftId;
    }

    public Integer getSkillNo() {
        return skillNo;
    }

    public void setSkillNo(Integer skillNo) {
        this.skillNo = skillNo;
    }

    public FtSkillVO getFtSkillVO() {
        return ftSkillVO;
    }

    public void setFtSkillVO(FtSkillVO ftSkillVO) {
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
        return "FtServiceVO [ftId=" + ftId + ", skillNo=" + skillNo + ", skillDesc=" + skillDesc + ", photo=" + (photo != null ? ("存在圖片, 大小=" + photo.length + " bytes") : "無圖片") + "]";
    }

    /**
     * 靜態內部類：複合主鍵類
     */
    public static class FtSkillId implements Serializable {
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
