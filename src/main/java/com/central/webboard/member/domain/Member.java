package com.central.webboard.member.domain;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Member {
    private String memberCode;
    private String memberId;
    private String memberPw;
    private String memberNick;
    private String memberEmail;
    private String memberCreateAt;
    private int roleNumber;
}
