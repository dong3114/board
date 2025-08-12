package com.central.webboard.utils.authority.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtTokenDTO {
    private final String token;
    private final String memberNo;
    private final Integer roleNumber;
    private final long expires;
}
