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

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service (value = "programService")
public class ProgramServiceImpl
    implements ProgramService {
    @Autowired
    ProgramRepository programrepos;

    @Autowired
    ClubRepository clubrepos;

    @Autowired
    private UserAuditing userAuditing;

    @Override
    public List<Program> findAll() {
        List<Program> list = new ArrayList<>();
        programrepos.findAll()
            .iterator()
            .forEachRemaining(list::add);
        return list;
    }

    @Override
    public Program findProgramById(long id) {
        return programrepos.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Program id " + id + " not found!"));
    }

    @Override
    public Program findByName(String name) throws ResourceNotFoundException{
        Program pr = programrepos.findByNameIgnoreCase(name);
        if(pr == null){
            throw new ResourceNotFoundException("Program " + name + " not found!");
        }
        return pr;
    }

    @Transactional
    @Override
    public void delete(long id) {
        if(programrepos.findById(id)
            .isPresent()){
            programrepos.deleteById(id);
        }else{
            throw new ResourceNotFoundException("Program id " + id + " not found!");
        }
    }

    @Transactional
    @Override
    public Program save(Program program) {
        if (program.getClubs()
            .size() > 0)
        {
            throw new ResourceFoundException("Club Programs are not updated through Program.");
        }

        return programrepos.save(program);
    }

    @Override
    public Program update(long id, Program program) {
        if(program.getName() == null){
            throw new ResourceNotFoundException("No program name found to update!");
        }
        if(program.getClubs().size() > 0){
            throw new ResourceFoundException("Club Programs are not updated through Programs.");
        }

        Program newProgram = findProgramById(id);
        programrepos.updateProgram(userAuditing.getCurrentAuditor().get(),
            id, program.getName());
        return findProgramById(id);
    }
}
