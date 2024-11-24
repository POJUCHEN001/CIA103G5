package com.cia103g5.user.favft.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.member.model.MemberVO;

@Entity
@Table(name = "fav_ft") // 表格名稱
public class FavFtVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private FavFtPK pk; // 複合主鍵
    private MemberVO memberInfo; // 外來鍵關聯到 member_info
    private FtVO ftInfo;         // 外來鍵關聯到 ft_info

    public FavFtVO() {
    }

    @EmbeddedId
    public FavFtPK getPk() {
        return pk;
    }

    public void setPk(FavFtPK pk) {
        this.pk = pk;
    }

    @ManyToOne
    @JoinColumn(name = "mem_id", insertable = false, updatable = false) // 外來鍵 mem_id
    public MemberVO getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberVO memberInfo) {
        this.memberInfo = memberInfo;
    }

    @ManyToOne
    @JoinColumn(name = "ft_id", insertable = false, updatable = false) // 外來鍵 ft_id
    public FtVO getFtInfo() {
        return ftInfo;
    }

    public void setFtInfo(FtVO ftInfo) {
        this.ftInfo = ftInfo;
    }
}

@Embeddable
class FavFtPK implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer memId; // 主鍵一部分
    private Integer ftId;  // 主鍵一部分

    public FavFtPK() {
    }

    public FavFtPK(Integer memId, Integer ftId) {
        this.memId = memId;
        this.ftId = ftId;
    }

    @Column(name = "mem_id", nullable = false)
    public Integer getMemId() {
        return memId;
    }

    public void setMemId(Integer memId) {
        this.memId = memId;
    }

    @Column(name = "ft_id", nullable = false)
    public Integer getFtId() {
        return ftId;
    }

    public void setFtId(Integer ftId) {
        this.ftId = ftId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(memId, ftId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        FavFtPK other = (FavFtPK) obj;
		return Objects.equals(memId, other.memId) && Objects.equals(ftId, other.ftId);
	}
    
}
