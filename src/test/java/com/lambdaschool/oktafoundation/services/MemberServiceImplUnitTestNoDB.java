package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.OktaFoundationApplicationTest;
import com.lambdaschool.oktafoundation.models.Member;
import com.lambdaschool.oktafoundation.repository.MemberRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OktaFoundationApplicationTest.class,
properties = {
        "command.line.runner.enabled=false"})
public class MemberServiceImplUnitTestNoDB
{
    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    private List<Member> memberList = new ArrayList<>();

    @Before
    public void setUp() throws Exception
    {
        Member mem1 = new Member(1, "Test001");
        Member mem2 = new Member(2, "Test002");
        Member mem3 = new Member(3, "Test003");

        memberList.add(mem1);
        memberList.add(mem2);
        memberList.add(mem3);

    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void findAll()
    {
        Mockito.when(memberRepository.findAll())
                .thenReturn(memberList);

        assertEquals(3,
                memberService.findAll()
                        .size());
    }

    @Test
    public void save()
    {
        Member m1 = new Member();
        m1.setMemberid("M12345ID");

        Mockito.when(memberRepository.save(any(Member.class)))
            .thenReturn(m1);

        assertEquals("M12345ID",
            memberService.save(m1)
                .getMemberid());
    }



    @Test
    public void saveNewMembers() throws IOException
    {
        Member m1 = new Member();
        m1.setMemberid("M12345ID");
        String memberid = "M12345ID";

        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        Mockito.when(bufferedReader.readLine()).thenReturn(memberid);

        Mockito.when(memberRepository.findMemberByMemberid(memberid))
            .thenReturn(null);
        Mockito.when(memberRepository.save(any(Member.class)))
            .thenReturn(m1);
        String testString = "memberid\nM12345ID";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());

        assertEquals(1,
            (memberService.saveNewMembers(testStream)).size());

    }
}