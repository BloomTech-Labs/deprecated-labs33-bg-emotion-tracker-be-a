package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Member;
import com.lambdaschool.oktafoundation.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "memberService")
public class MemberServiceImpl implements MemberService
{

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<Member> findAll()
    {
        List<Member> list = new ArrayList<>();

        memberRepository.findAll()
                .iterator()
                .forEachRemaining(list::add);
        return list;
    }


    @Transactional
    @Override
    public Member save(Member member)
    {
        Member newMember = new Member();
        return newMember;
    }
}