package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.OktaFoundationApplicationTest;
import com.lambdaschool.oktafoundation.models.Club;
import com.lambdaschool.oktafoundation.models.Member;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.repository.ClubRepository;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    @MockBean
    ClubRepository clubrepos;

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

    @Test
    public void saveNewPrograms() throws IOException{
        String programname = "guitar lessons";
        String clubname = "club1";
        String datarow = programname + "," + clubname;

        Program newprogram = new Program();
        newprogram.setName(programname);
        newprogram.setProgramid(10);

        Club currentclub = new Club();
        currentclub.setClubname("club1");
        currentclub.setClubdirector("");
        currentclub.setClubid(100);

        BufferedReader bufferedReader = Mockito.mock(BufferedReader.class);
        Mockito.when(bufferedReader.readLine()).thenReturn(datarow);

        Mockito.when(clubrepos.findByClubnameIgnoreCase(clubname))
            .thenReturn(currentclub);

        Mockito.when(programrepos.findByNameIgnoreCase(programname))
            .thenReturn(null);

        Mockito.when(programrepos.save(any(Program.class)))
            .thenReturn(newprogram);

        String testString = "Program Name,Club\nguitarlessons,club1";
        InputStream testStream = new ByteArrayInputStream(testString.getBytes());

        assertEquals(1,
            (programService.saveNewPrograms(testStream)).size());
    }
}
