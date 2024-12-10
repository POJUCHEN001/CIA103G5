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
    private String nickName;
    private Integer status;

}
