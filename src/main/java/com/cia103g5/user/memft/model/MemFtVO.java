package com.cia103g5.user.memft.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.member.model.MemberVO;

@Entity
@Table(name = "fav_ft")
@IdClass(MemFtVO.MemFtId.class) // 使用內部類作為組合鍵
public class MemFtVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "mem_id", nullable = false)
    @NotNull(message = "會員編號: 請勿空白")
    private MemberVO memId;

    @Id
    @ManyToOne
    @JoinColumn(name = "ft_id", nullable = false)
    @NotNull(message = "占卜師編號: 請勿空白")
    private FtVO ftId;

    public MemFtVO() {
    }

    public MemberVO getMemId() {
        return memId;
    }

    public void setMemId(MemberVO memId) {
        this.memId = memId;
    }

    public FtVO getFtId() {
        return ftId;
    }

    public void setFtId(FtVO ftId) {
        this.ftId = ftId;
    }

    /**
     * 靜態內部類：複合主鍵類
     */
    public static class MemFtId implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer memId;
        private Integer ftId;

        public MemFtId() {
        }

        public MemFtId(Integer memId, Integer ftId) {
            this.memId = memId;
            this.ftId = ftId;
        }

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

        @Override
        public int hashCode() {
            return Objects.hash(ftId, memId);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            MemFtId other = (MemFtId) obj;
            return Objects.equals(ftId, other.ftId) && Objects.equals(memId, other.memId);
        }
    }
}
