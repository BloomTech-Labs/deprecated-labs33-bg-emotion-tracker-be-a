package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.OktaFoundationApplicationTest;
import com.lambdaschool.oktafoundation.models.*;
import com.lambdaschool.oktafoundation.repository.ClubRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OktaFoundationApplicationTest.class,
    properties = {
        "command.line.runner.enabled=false"})
public class ClubServiceImplUnitTestNoDB
{

    @Autowired
    private ClubService clubService;

    @MockBean
    private ClubRepository clubRepository;

    @MockBean
    private ProgramService programService;

    private List<Club> clubList;

    @Before
    public void setUp() throws Exception
    {
        clubList = new ArrayList<>();

        Program p1 = new Program("Club Checkin");
        p1.setProgramid(1);
        Program p2 = new Program("Club Checkout");
        p2.setProgramid(2);
        Program p3 = new Program("Football");
        p3.setProgramid(3);
        Program p4 = new Program("Basketball");
        p4.setProgramid(4);
        Program p5 = new Program("Baseball");
        p5.setProgramid(5);


        Club c1 = new Club( "club1", "llama002@maildrop.cc");
        c1.getPrograms()
            .add(new ClubPrograms(c1,p1));
        c1.getPrograms()
            .add(new ClubPrograms(c1,p2));
        c1.getPrograms()
            .add(new ClubPrograms(c1,p3));
        c1.getPrograms()
            .add(new ClubPrograms(c1,p4));
        c1.getPrograms()
            .add(new ClubPrograms(c1,p5));
        clubList.add(c1);

        Club c2 = new Club( "club2", "llama003@maildrop.cc");
        c2.getPrograms()
            .add(new ClubPrograms(c2,p1));
        c2.getPrograms()
            .add(new ClubPrograms(c2,p2));
        c2.getPrograms()
            .add(new ClubPrograms(c2,p4));
        clubList.add(c2);

        Club c3 = new Club( "club3",  "llama004@maildrop.cc");
        c3.getPrograms()
            .add(new ClubPrograms(c3,p1));
        c3.getPrograms()
            .add(new ClubPrograms(c3,p2));
        c3.getPrograms()
            .add(new ClubPrograms(c3,p3));
        c3.getPrograms()
            .add(new ClubPrograms(c3,p4));
        clubList.add(c3);

        System.out.println("\n*** Seed Data ***");
        for (Club c : clubList)
        {
            System.out.println(c.getClubid() + " " + c.getClubname());
        }
        System.out.println("*** Seed Data ***\n");

        MockitoAnnotations.initMocks(this);

    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void findAll()
    {
        Mockito.when(clubRepository.findAll())
            .thenReturn(clubList);

        assertEquals(3,
            clubService.findAll()
                .size());
    }

    @Test
    public void save()
    {
        Program p2 = new Program("testbasketball");
        p2.setProgramid(2);

        Club c2 = new Club("club2", "llama003@maildrop.cc");
        c2.getPrograms()
            .add(new ClubPrograms(c2,p2));


        Mockito.when(programService.findProgramById(2L))
            .thenReturn(p2);

        Mockito.when(clubRepository.save(any(Club.class)))
            .thenReturn(c2);

        assertEquals("llama003@maildrop.cc",
            clubService.save(c2)
                .getClubdirector());
    }

    @Test
    public void deleteAll()
    {
        Mockito.doNothing()
            .when(clubRepository)
            .deleteAll();

        clubService.deleteAll();
        assertEquals(3,
            clubList.size());

    }
}