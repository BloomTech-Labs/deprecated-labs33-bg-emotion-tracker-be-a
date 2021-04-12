package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Member;

import java.util.List;

public interface MemberService
{
    List<Member> findAll();

    Member save(Member member);
}
