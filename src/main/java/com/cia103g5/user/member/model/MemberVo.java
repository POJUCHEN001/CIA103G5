package com.cia103g5.user.member.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**##########################
 #                          #
 #      會員 model           #
 #                          #
 ##########################*/

@Getter
@Setter
@ToString
@Entity
@Table(name = "member_info")
public class MemberVo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mem_id")
    private Integer memId;

    @Column(name = "ft_id")
    private Integer ftId;

    private String name;

    private String account;

    private String password;

    private String email;

    @Column(name = "email_state")
    private Integer emailState;

    @Column(name = "registered_time")
    private Date registeredTime;

    private String phone;

    private String gender;

    private String nickname;

    private Integer status;

    private Integer points;
}
