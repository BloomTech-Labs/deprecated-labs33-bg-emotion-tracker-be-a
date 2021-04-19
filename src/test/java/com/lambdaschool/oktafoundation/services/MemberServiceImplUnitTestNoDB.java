package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.OktaFoundationApplicationTest;
import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Member;
import com.lambdaschool.oktafoundation.repository.MemberRepository;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import java.util.Optional;

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
    public void saveNewMember()
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

    @Test
    public void findMemberByJavaId()
    {
        Mockito.when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(memberList.get(0)));
        assertEquals("Test001", memberService.findMemberByJavaId(1L).getMemberid());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findUserByJavaIdNotFound()
    {
        Mockito.when(memberRepository.findById(1L))
                .thenReturn(Optional.empty());
        assertEquals("Test001",
                memberService.findMemberByJavaId(1L)
                        .getMemberid());
    }

    @Test
    public void findMemberByStringId()
    {
        Mockito.when(memberRepository.findMemberByMemberid(any(String.class)))
                .thenReturn(memberList.get(0));

        assertEquals("Test001", memberService.findMemberByStringId("Test001").getMemberid());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void findMemberByStringIdNotFound()
    {
        Mockito.when(memberRepository.findMemberByMemberid("notauser"))
                .thenReturn(null);
        assertEquals("notauser",
                memberService.findMemberByStringId("notauser")
                        .getMemberid());
    }

    @Test
    public void findByIdContaining()
    {
        Mockito.when(memberRepository.findMembersByMemberidContaining(any(String.class)))
                .thenReturn(memberList);

        assertEquals(3, memberService.findByIdContaining("Test").size());
    }

    @Test
    public void delete()
    {
        Mockito.when(memberRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(memberList.get(0)));
        Mockito.doNothing()
                .when(memberRepository)
                .deleteById(1L);
        memberService.delete(1L);
        assertEquals(3,memberList.size() );
    }

    @Test(expected = ResourceNotFoundException.class)
    public void notFoundDelete()
    {
        Mockito.when(memberRepository.findById(999L))
                .thenReturn(Optional.empty());
        Mockito.doNothing()
                .when(memberRepository)
                .deleteById(999L);
        memberService.delete(999L);
        assertEquals(3,memberList.size() );
    }
}