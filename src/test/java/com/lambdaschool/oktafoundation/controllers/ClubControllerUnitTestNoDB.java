package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.OktaFoundationApplication;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.repository.ClubRepository;
import com.lambdaschool.oktafoundation.repository.UserRepository;
import com.lambdaschool.oktafoundation.services.ClubService;
import com.lambdaschool.oktafoundation.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;


@RunWith (SpringRunner.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = OktaFoundationApplication.class,
    properties = {
        "command.line.runner.enabled=false"})
@AutoConfigureMockMvc
@WithMockUser (username = "llama001maildrop.cc",
    roles = {"SUPERADMIN"})
public class ClubControllerUnitTestNoDB {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private ClubService clubService;

    @MockBean
    private ClubRepository clubrepos;

    @MockBean
    private UserService userService;
    private List<Club> clubList;

    @MockBean
    private UserRepository userrepos;
    private List<User> userList;
    private User u1;

    @Before
    public void setUp() throws Exception{
        clubList = new ArrayList<>();
        userList = new ArrayList<>();

        Role r1 = new Role("superadmin");
        r1.setRoleid(1);

        u1 = new User("llama001@maildrop.cc");
        u1.getRoles().add(new UserRoles(u1, r1));

        u1.setUserid(101);
        userList.add(u1);

        Club c1 = new Club("jackson", "llama002@maildrop.cc");
        Club c2 = new Club("red river", "llama002@maildrop.cc");
        Club c3 = new Club("bay view", "llama002@maildrop.cc");

        c1.setClubid(50);
        c2.setClubid(60);
        c3.setClubid(70);

        c1.getPrograms().add(new ClubPrograms(c1, new Program("volleyball")));
        c2.getPrograms().add(new ClubPrograms(c2, new Program("tennis")));
        c3.getPrograms().add(new ClubPrograms(c3, new Program("softball")));

        clubList.add(c1);
        clubList.add(c2);
        clubList.add(c3);

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @After
    public void tearDown() throws Exception{

    }

    @Test
    public void listAllClubs() throws Exception{
        String apiUrl = "/clubs/clubs";
        Mockito.when(userrepos.findByUsername(u1.getUsername()))
            .thenReturn(u1);
        Mockito.when(clubService.findAll())
            .thenReturn(clubList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
            .andReturn();
        String tr = r.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(clubList);

        assertEquals(er,tr);
    }

    @Test
    public void uploadClubs() throws Exception{
        String apiUrl = "/clubs/upload";
        Mockito.when(userrepos.findByUsername(u1.getUsername()))
            .thenReturn(u1);

        Mockito.when(clubService.saveNewClubs(any(InputStream.class)))
            .thenReturn(clubList);

        MockMultipartFile testFile = new MockMultipartFile("csvfile", "clubs.csv", "text/csv", "Club Name, Program\nbay view,basketballl".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart(apiUrl)
            .file(testFile))
            .andExpect(status().is2xxSuccessful());
    }

}
