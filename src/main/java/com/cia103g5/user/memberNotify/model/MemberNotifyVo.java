package com.cia103g5.user.memberNotify.model;

import com.cia103g5.user.member.model.MemberVO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "member_notify")
@Getter
@Setter
public class MemberNotifyVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notify_no")
    private Integer notifyNo;

    @Column(name = "notify_type", nullable = false)
    private String notifyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mem_id", referencedColumnName = "mem_id")
    private MemberVO member;

    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "notified_time", nullable = false)
    private Date notifiedTime;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private Integer status;

    @Column(name = "seed_mail", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean seedMail;

    // 無參構造函數
    public MemberNotifyVo() {}

    public MemberNotifyVo(String notifyType, MemberVO member, String title, String content,
                          Date notifiedTime, Integer status, Boolean seedMail) {
        this.notifyType = notifyType;
        this.member = member;
        this.title = title;
        this.content = content;
        this.notifiedTime = notifiedTime;
        this.status = status;
        this.seedMail = seedMail;
    }
}
