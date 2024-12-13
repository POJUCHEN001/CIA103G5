package com.cia103g5.user.favft.model;

import java.io.Serializable;
import java.util.Objects;

import com.cia103g5.user.ft.model.FtVO;
import com.cia103g5.user.member.model.MemberVO;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

/**
 * 複合表：會員收藏占卜師 fav_ft
 */

@Entity
@Table(name = "fav_ft")
public class FavFtVO implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FavFtKey id;

	@ManyToOne
	@MapsId("memId")
	@JoinColumn(name = "mem_id", referencedColumnName = "mem_id")
	private MemberVO memberVO;

	@ManyToOne
	@MapsId("ftId")
	@JoinColumn(name = "ft_id", referencedColumnName = "ft_id")
	private FtVO ftVO;

	public FavFtVO() {
	}

	public FavFtKey getId() {
		return id;
	}

	public void setId(FavFtKey id) {
		this.id = id;
	}

	public MemberVO getMemberVO() {
		return memberVO;
	}

	public void setMemberVO(MemberVO memberVO) {
		this.memberVO = memberVO;
	}

	public FtVO getFtVO() {
		return ftVO;
	}

	public void setFtVO(FtVO ftVO) {
		this.ftVO = ftVO;
	}

	@Override
	public String toString() {
		return "FavFtVO [id=" + id + ", memberVO=" + memberVO + ", ftVO=" + ftVO + "]";
	}

	@Embeddable
	public static class FavFtKey implements Serializable {
		private static final long serialVersionUID = 1L;

		@Column(name = "mem_id")
		private Integer memId;

		@Column(name = "ft_id")
		private Integer ftId;

		public FavFtKey() {
		}

		public FavFtKey(Integer memId, Integer ftId) {
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
			return Objects.hash(memId, ftId);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null || getClass() != obj.getClass())
				return false;
			FavFtKey other = (FavFtKey) obj;
			return Objects.equals(memId, other.memId) && Objects.equals(ftId, other.ftId);
		}

		@Override
		public String toString() {
			return "FavFtKey [memId=" + memId + ", ftId=" + ftId + "]";
		}

	}

}
