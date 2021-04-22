package com.lambdaschool.oktafoundation.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.oktafoundation.OktaFoundationApplicationTest;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.repository.ProgramRepository;
import com.lambdaschool.oktafoundation.repository.UserRepository;
import com.lambdaschool.oktafoundation.services.ProgramService;
import com.lambdaschool.oktafoundation.services.UserService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.parameters.P;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith (SpringRunner.class)
@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = OktaFoundationApplicationTest.class,
    properties = {
        "command.line.runner.enabled=false"})
@AutoConfigureMockMvc
@WithMockUser (username = "llama001@maildrop.cc",
    roles = {"SUPERADMIN"})
public class ProgramControllerUnitTestNoDB {
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;

    @MockBean
    private ProgramService programService;

    @MockBean
    private ProgramRepository programrepos;

    @MockBean
    private UserService userService;
    private List<Program> programList;

    @MockBean
    private UserRepository userrepos;
    private List<User> userList;
    private User u1;

    @Before
    public void setUp() throws Exception{
        programList = new ArrayList<>();
        userList = new ArrayList<>();

        Role r1 = new Role("superadmin");
        r1.setRoleid(1);

        u1 = new User("llama001@maildrop.cc");
        u1.getRoles().add(new UserRoles(u1, r1));

        u1.setUserid(101);
        userList.add(u1);
;
        Program p1 = new Program("volleyball");
        Program p2 = new Program("tennis");
        Program p3 = new Program("softball");
        p1.setProgramid(10);
        p2.setProgramid(20);
        p3.setProgramid(30);
        p1.getClubs().add(new ClubPrograms(new Club("club1","llama002@maildrop.cc"), p1));
        p2.getClubs().add(new ClubPrograms(new Club("club1","llama002@maildrop.cc"), p2));
        p3.getClubs().add(new ClubPrograms(new Club("club1","llama002@maildrop.cc"), p3));

        programList.add(p1);
        programList.add(p2);
        programList.add(p3);

        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @After
    public void tearDown() throws Exception{

    }

    @Test
    public void listAllPrograms() throws  Exception{
        String apiUrl = "/programs/programs";
        Mockito.when(userrepos.findByUsername(u1.getUsername()))
            .thenReturn(u1);

        Mockito.when(programService.findAll())
            .thenReturn(programList);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
            .andReturn();
        String tr = r.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(programList);

        assertEquals(er, tr);
    }

    @Test
    public void uploadPrograms() throws Exception
    {
        String apiUrl = "/programs/upload";
        Mockito.when(userrepos.findByUsername(u1.getUsername()))
            .thenReturn(u1);

        Mockito.when(programService.saveNewPrograms(any(InputStream.class)))
            .thenReturn(programList);

        MockMultipartFile testFile = new MockMultipartFile("csvfile", "programs.csv", "text/csv", "Program Name,Club\nbasketball,club1".getBytes());
        mockMvc.perform(MockMvcRequestBuilders.multipart(apiUrl)
            .file(testFile))
            .andExpect(status().is2xxSuccessful());
    }
}
