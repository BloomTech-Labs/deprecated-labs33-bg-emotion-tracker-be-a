package com.lambdaschool.oktafoundation.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.oktafoundation.OktaFoundationApplicationTest;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.repository.MemberRepository;
import com.lambdaschool.oktafoundation.repository.UserRepository;
import com.lambdaschool.oktafoundation.services.MemberService;
import com.lambdaschool.oktafoundation.services.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = OktaFoundationApplicationTest.class,
        properties = {
                "command.line.runner.enabled=false"})
@AutoConfigureMockMvc
@WithMockUser(username = "llama001@maildrop.cc",
        roles = {"SUPERADMIN"})
public class MemberControllerUnitTestNoDB {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    @MockBean
    private MemberService memberService;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private UserService userService;
    private List<Member> memberList;
    @MockBean
    private UserRepository userrepos;
    private List<User> userList;
    private User u1;
    @Before
    public void setUp() throws Exception
    {
        memberList = new ArrayList<>();
        userList = new ArrayList<>();

        Role r1 = new Role("superadmin");
        r1.setRoleid(1);

        // super admin
        u1 = new User("llama001@maildrop.cc");
        u1.getRoles()
                .add(new UserRoles(u1,
                        r1));

        u1.getUseremails()
                .add(new Useremail(u1,
                        "llama001@maildrop.cc"));
        u1.getUseremails()
                .get(0)
                .setUseremailid(10);

        u1.setUserid(101);
        userList.add(u1);

        Member mem1 = new Member(1, "Test001");
        Member mem2 = new Member(2, "Test002");
        Member mem3 = new Member(3, "Test003");

        memberList.add(mem1);
        memberList.add(mem2);
        memberList.add(mem3);

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }
    @After
    public void tearDown() throws Exception {
    }
    @Test
    public void listAllMembers()  throws  Exception
    {
        String apiUrl = "/members/members";
        Mockito.when(userrepos.findByUsername(u1.getUsername()))
                .thenReturn(u1);

        Mockito.when(memberService.findAll())
                .thenReturn(memberList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn();
        String tr = r.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(memberList);

        assertEquals(er, tr);
    }

    @Test
    public void uploadMembers() throws Exception
    {
        String apiUrl = "/members/upload";
        Mockito.when(userrepos.findByUsername(u1.getUsername()))
            .thenReturn(u1);

        Mockito.when(memberService.saveNewMembers(any(InputStream.class)))
            .thenReturn(memberList);

        MockMultipartFile testFile = new MockMultipartFile("csvfile", "members.csv", "text/csv", "memberid\nM12345ID".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart(apiUrl)
            .file(testFile))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void uploadNoNewMembers() throws Exception
    {
        String apiUrl = "/members/upload";
        Mockito.when(userrepos.findByUsername(u1.getUsername()))
            .thenReturn(u1);

        List<Member> noMembers = new ArrayList<>();
        Mockito.when(memberService.saveNewMembers(any(InputStream.class)))
            .thenReturn(noMembers);

        MockMultipartFile testFile = new MockMultipartFile("csvfile", "members.csv", "text/csv", "memberid\nM12345ID".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart(apiUrl)
            .file(testFile))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void addNewMember() throws Exception
    {
        String apiUrl = "/members/member";

        Mockito.when(userrepos.findByUsername(u1.getUsername()))
                .thenReturn(u1);

        Mockito.when(memberService.saveNewMember("M12345ID")).thenReturn(memberList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("M12345ID");

        mockMvc.perform(rb)
                .andExpect(status().isCreated());
    }

    @Test
    public void getMemberbyJavaID() throws Exception
    {
        String apiUrl = "/members/member/1";

        Mockito.when(userrepos.findByUsername(u1.getUsername()))
                .thenReturn(u1);

        Mockito.when(memberService.findMemberByJavaId(1L))
                .thenReturn(memberList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn(); // this could throw an exception
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(memberList.get(0));

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("Rest API Returns List",
                er,
                tr);
    }

    @Test
    public void getMemberbyMemberID() throws Exception
    {
        String apiUrl = "/members/member/id/m12";

        Mockito.when(userrepos.findByUsername(u1.getUsername()))
                .thenReturn(u1);

        Mockito.when(memberService.findMemberByStringId("m12"))
                .thenReturn(memberList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl);

        MvcResult r = mockMvc.perform(rb)
                .andReturn();

        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(memberList.get(0));

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("Rest API Returns List",
                er,
                tr);

    }

    @Test
    public void getMemberLikeID() throws Exception
    {
        String apiUrl = "/members/member/id/like/m12";

        Mockito.when(userrepos.findByUsername(u1.getUsername()))
                .thenReturn(u1);

        Mockito.when(memberService.findByIdContaining(any(String.class)))
                .thenReturn(memberList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl);

        MvcResult r = mockMvc.perform(rb)
                .andReturn(); // this could throw an exception

        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(memberList);

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("Rest API Returns List",
                er,
                tr);
    }

    @Test
    public void deleteMemberByJavaId() throws Exception
    {
        String apiUrl = "/members/member/1";

        Mockito.when(userrepos.findByUsername(u1.getUsername()))
                .thenReturn(u1);

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(rb)
                .andExpect(status().is2xxSuccessful())
                .andDo(MockMvcResultHandlers.print());
    }
}