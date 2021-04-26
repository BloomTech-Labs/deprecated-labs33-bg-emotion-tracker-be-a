package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.exceptions.ResourceFoundException;
import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Club;
import com.lambdaschool.oktafoundation.models.ClubPrograms;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.repository.ClubRepository;
import com.lambdaschool.oktafoundation.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Transactional
@Service(value = "clubService")
public class ClubServiceImpl implements ClubService
{
    @Autowired
    ClubRepository clubRepository;

    @Autowired
    ProgramService programService;

    private ClubRepository clubrepos;

    private ProgramRepository programrepos;

    @Override
    public List<Club> findAll()
    {
        List<Club> list = new ArrayList<>();

        clubRepository.findAll()
                .iterator()
                .forEachRemaining(list::add);
        return list;
    }

    @Transactional
    @Override
    public Club save(Club club) {
        if (club.getPrograms()
            .size() > 0)
        {
            throw new ResourceFoundException("Club Programs are not updated through Program.");
        }

        return clubrepos.save(club);
    }

    @Override
    public void deleteAll()
    {
        clubRepository.deleteAll();
    }

    @Override
    public List<Club> saveNewClubs(InputStream stream) throws IOException
    {
        List<Club> addedClubs = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String programname;
        String clubname;
        String row;
        // removes header line from CSV file
        String headerLine = reader.readLine();
        String[] columnheaders = headerLine.split(",");
        List<String> columnheaderlist = Arrays.asList(columnheaders);
        ArrayList<String> listofheaders = new ArrayList<>(columnheaderlist);

        // Validate two columns in headers
        if(listofheaders.size() != 2){
            throw new ResourceNotFoundException("CSV file must contain Program Name and Club headers");
        }

        // Validate correct headers
        if (!(listofheaders.get(0).equalsIgnoreCase("Club Name") && listofheaders.get(1).equalsIgnoreCase("Program")))
        {
            throw new ResourceNotFoundException("CSV file must contain CLub Name and Program headers, file contains " + listofheaders.get(0) + " and " + listofheaders.get(1));
        }

        while((row = reader.readLine())!= null)
        {
            String[] rowarray = row.split(",");
            List<String> rowarraylist = Arrays.asList(rowarray);
            List<String> fields = new ArrayList<>(rowarraylist);

            programname = fields.get(1).toLowerCase();
            programname = programname.replaceAll("^\"|\"$",
                "");
            clubname = fields.get(0).toLowerCase();
            clubname = clubname.replaceAll("^\"|\"$",
                "");

            // if club, doesn't exist throw resource not found
            // clubs are not updated through programs
            Program p = programrepos.findByNameIgnoreCase(programname);
            if (p == null)
            {
                throw new ResourceNotFoundException("Program " + programname + " not found");
            }

            // if program doesn't exist, create it
            Club c = clubrepos.findByClubnameIgnoreCase(clubname);
            if (c == null)
            {
                c = clubrepos.save(new Club(clubname));

            }

            // verify program is not already associated with this club
            // loop through all the programs clubs
            // if any clubprogram has the clubname being added
            // set the boolean flag to false
            // if after the loop, boolean is true
            // add the new relationship
            boolean isNewClubProgram = true;
            for (ClubPrograms cp : c.getPrograms())
            {
                if(cp.getProgram().getName().equalsIgnoreCase(p.getName())){
                    isNewClubProgram = false;
                }
            }

            if (isNewClubProgram)
            {
                c.getPrograms()
                    .add(new ClubPrograms(c,
                        p));
                addedClubs.add(c);
            }

        }
        return addedClubs;
    }

}
