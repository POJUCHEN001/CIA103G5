package com.cia103g5.user.member.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SessionMemberDTO implements Serializable {

    private Integer memberId;
    private String name;
    private String nickName;
    private Integer status;
//    private Boolean isFortuneTeller;
    private Integer ftId;

}
