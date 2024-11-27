package com.cia103g5.user.avaliabletime.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

import com.cia103g5.user.ft.model.FtVO;

@Entity
@Table(name = "avaliable_time")
public class AvaliableTimeVO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private Integer avaliableTimeNo;
    private FtVO ftId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;

    public AvaliableTimeVO() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "avaliable_time_no")
    public Integer getAvaliableTimeNo() {
        return avaliableTimeNo;
    }

    public void setAvaliableTimeNo(Integer avaliableTimeNo) {
        this.avaliableTimeNo = avaliableTimeNo;
    }

    @ManyToOne
    @JoinColumn(name = "ft_id", nullable = false)
    @NotNull(message = "占卜師編號: 請勿空白")
    public FtVO getFtId() {
        return ftId;
    }

    public void setFtId(FtVO ftId) {
        this.ftId = ftId;
    }

    @Column(name = "start_time")
    @NotNull(message = "開始時間: 請勿空白")
    public Timestamp getStartTime() {
        return startTime;
    }

    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time")
    @NotNull(message = "結束時間: 請勿空白")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Column(name = "status", nullable = false)
    @NotNull(message = "狀態: 請勿空白")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
