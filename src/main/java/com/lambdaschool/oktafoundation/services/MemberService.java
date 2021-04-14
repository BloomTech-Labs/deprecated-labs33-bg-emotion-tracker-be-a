package com.lambdaschool.oktafoundation.services;

import com.github.javafaker.Bool;
import com.lambdaschool.oktafoundation.models.Member;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface MemberService
{
    List<Member> findAll();

    Member save(Member member);

    List<Member> saveNewMembers(InputStream stream) throws IOException;

}
