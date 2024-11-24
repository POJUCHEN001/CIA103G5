package com.cia103g5.user.ft.model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ft_grade")
public class FtGrade implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ft_rank")
	private Integer ftRank;

	@Column(name = "rank_name")
	private String rankName;
	

	// 建構子
	public FtGrade() {
		super();
	}

	public FtGrade(Integer ftRank, String rankName) {
		super();
		this.ftRank = ftRank;
		this.rankName = rankName;
	}

	// getter & Setter
	public Integer getFtRank() {
		return ftRank;
	}

	public void setFtRank(Integer ftRank) {
		this.ftRank = ftRank;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
	}

	@Override
	public String toString() {
		return "FtGrade [ftRank=" + ftRank + ", rankName=" + rankName + "]";
	}
	

}
