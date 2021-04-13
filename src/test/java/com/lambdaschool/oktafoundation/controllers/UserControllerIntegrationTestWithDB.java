package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.OktaFoundationApplicationTest;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration test for UserController so only looking at 100% coverage on UserController
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = OktaFoundationApplicationTest.class)
@AutoConfigureMockMvc
@WithMockUser(username = "llama001@maildrop.cc",
    roles = {"SUPERADMIN"})
public class UserControllerIntegrationTestWithDB
{
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Before
    public void setUp()
    {
        RestAssuredMockMvc.webAppContextSetup(webApplicationContext);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void whenMeasuredResponseTime() throws
                                           Exception
    {
        long time = System.currentTimeMillis();
        this.mockMvc.perform(get("/users/users"))
            .andDo(print());
        long responseTime = (System.currentTimeMillis() - time);

        assertTrue("timestamp",
            (responseTime < 5000L));
    }

    @Test
    public void getAllUsers() throws
                              Exception
    {
        this.mockMvc.perform(get("/users/users"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("llama001")));
    }

    @Test
    public void getUserInfo() throws
                              Exception
    {
        this.mockMvc.perform(get("/users/getuserinfo"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("llama001")));
    }

    @Test
    public void getUserLikeName() throws
                                  Exception
    {
        this.mockMvc.perform(get("/users/user/name/like/{userName}",
            "lla"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("llama001")));
    }

    @Test
    public void getUserById() throws
                              Exception
    {
        this.mockMvc.perform(get("/users/user/{userid}",
            6))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("llama002")));
    }

    @Test
    public void getUserByIdNotFound() throws
                                      Exception
    {
        this.mockMvc.perform(get("/users/user/{userid}",
            999))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(content().string(containsString("ResourceNotFoundException")));
    }

    @Test
    public void getUserByName() throws
                                Exception
    {
        this.mockMvc.perform(get("/users/user/name/{userName}",
            "llama003@maildrop.cc"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("llama003@maildrop.cc")));
    }

    @Test
    public void getUserByNameNotFound() throws
                                        Exception
    {
        this.mockMvc.perform(get("/users/user/name/{userName}",
            "lambda"))
            .andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(content().string(containsString("ResourceNotFoundException")));
    }

    @Test
    public void givenPostAUser() throws
                                 Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/user")
            .content("{\"username\": \"llama009@maildrop.cc\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(MockMvcResultMatchers.header()
                .exists("location"));
    }

    @Test
    public void givenPutAUser() throws
                                Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.put("/users/user/11")
            .content("{\"username\": \"testllama007@maildrop.cc\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    public void deleteUserById() throws
                                 Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/user/{id}",
            11))
            .andDo(print())
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void deleteUserByIdNotFound() throws
                                         Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.delete("/users/user/{id}",
            999))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    public void UpdateUser() throws
                             Exception
    {
        mockMvc.perform(MockMvcRequestBuilders.patch("/users/user/{userid}",
            10)
            .content("{\"username\": \"testllama006@maildrop.cc\"}")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());
    }
}