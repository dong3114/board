package com.central.webboard.member.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MemberMapper {
    // 회원 아이디 중복체크
    boolean checkById(@Param("memberId") String memberId);
    // 회원 가입
    void insertMember(@Param("memberId") String memberId, @Param("memberPw") String memberPw);


}
