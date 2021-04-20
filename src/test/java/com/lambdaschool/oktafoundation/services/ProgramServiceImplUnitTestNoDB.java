package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.OktaFoundationApplicationTest;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.repository.ProgramRepository;
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
import java.util.ArrayList;
import java.util.List;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith (SpringRunner.class)
@SpringBootTest (classes = OktaFoundationApplicationTest.class,
    properties = {
        "command.line.runner.enabled=false"})
public class ProgramServiceImplUnitTestNoDB {
    @Autowired
    private ProgramService programService;

    @MockBean
    ProgramRepository programrepos;

    private List<Program> programList = new ArrayList<>();

    @Before
    public void setUp() throws Exception{
        Program p1 = new Program("volleyball");
        Program p2 = new Program("tennis");
        Program p3 = new Program("softball");

        programList.add(p1);
        programList.add(p2);
        programList.add(p3);
    }

    @After
    public void tearDown() throws Exception{

    }

    @Test
    public void findAll(){
        Mockito.when(programrepos.findAll())
            .thenReturn(programList);

        assertEquals(3, programService.findAll().size());
    }

    @Test
    public void save(){
        Program p1 = new Program();
        p1.setName("track");

        Mockito.when(programrepos.save(any(Program.class)))
            .thenReturn(p1);

        assertEquals("track", programService.save(p1).getName());
    }
}
