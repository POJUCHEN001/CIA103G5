package com.cia103g5.user.ftskill.model;

import java.util.Objects;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.skill.model.SkillVO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ft_skill")
@IdClass(FtSkillVO.FtSkillId.class) // 使用內部類作為組合鍵
public class FtSkillVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer ftId;
    private Integer skillNo;
    private FtVO ftVO;
    private SkillVO skillVO;

    public FtSkillVO() {
    }

    @Id
    @NotNull(message = "占卜師編號: 請勿空白")
    public Integer getFtId() {
        return ftId;
    }

    public void setFtId(Integer ftId) {
        this.ftId = ftId;
    }

    @Id
    @NotNull(message = "專長編號: 請勿空白")
    public Integer getSkillNo() {
        return skillNo;
    }

    public void setSkillNo(Integer skillNo) {
        this.skillNo = skillNo;
    }

    @ManyToOne
    @JoinColumn(name = "ft_id", insertable = false, updatable = false)
    public FtVO getFtVO() {
        return ftVO;
    }

    public void setFtVO(FtVO ftVO) {
        this.ftVO = ftVO;
    }

    @ManyToOne
    @JoinColumn(name = "skill_no", insertable = false, updatable = false)
    public SkillVO getSkillVO() {
        return skillVO;
    }

    public void setSkillVO(SkillVO skillVO) {
        this.skillVO = skillVO;
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
			FtSkillId other = (FtSkillId) obj;
			return Objects.equals(ftId, other.ftId) && Objects.equals(skillNo, other.skillNo);
		}

    }
}
