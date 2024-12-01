package com.cia103g5.user.avaliabletime.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import com.cia103g5.user.ft.model.FtVO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "available_time")
public class AvailableTimeVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "available_time_no")
    private Integer availableTimeNo;

    @ManyToOne
    @JoinColumn(name = "ft_id", nullable = false)
    @NotNull(message = "占卜師編號: 請勿空白")
    private FtVO ftId;

    @Column(name = "start_time")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "開始時間: 請勿空白")
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(TemporalType.TIMESTAMP)
    @NotNull(message = "結束時間: 請勿空白")
    private Date endTime;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    @NotNull(message = "狀態: 請勿空白")
    private Integer status;

    public AvailableTimeVO() {
    }


}
