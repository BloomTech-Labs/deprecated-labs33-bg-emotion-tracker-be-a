package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Member;
import com.lambdaschool.oktafoundation.repository.MemberRepository;
import org.hibernate.annotations.ManyToAny;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        newMember.setMemberid(member.getMemberid());
        memberRepository.save(newMember);
        return newMember;
    }

    @Transactional
    @Override
    public List<Member> saveNewMembers(InputStream stream) throws IOException
    {
        List<Member> newMembers = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String member;

        while((member = reader.readLine())!= null)
        {
            member = member.replace("\n", "");
            if(! (member.equals("memberid") || member.equals("\n")))
            {
                Member isCurrentMember = memberRepository.findMemberByMemberid(member);
                if ( isCurrentMember == null )
                {
                    Member newMember = new Member();
                    newMember.setMemberid(member);
                    Member addedMember = save(newMember);
                    newMembers.add(addedMember);
                }
            }
        }
        return newMembers;
    }
}