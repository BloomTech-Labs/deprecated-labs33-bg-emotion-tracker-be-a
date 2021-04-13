package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.ClubPrograms;
import com.lambdaschool.oktafoundation.models.Program;
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
    public Program findProgramById(long id) throws ResourceNotFoundException {
        return programrepos.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Program id " + id + " not found!"))
    }

    @Override
    public Program findProgramByName(String name) throws ResourceNotFoundException{
        Program program = programrepos.findByName(name);
        if(program == null){
            throw new ResourceNotFoundException("Program " + name + " not found!");
        }
        return program;
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
        Program newProgram = new Program();
        if(program.getProgramid() != 0){
            programrepos.findById(program.getProgramid())
                .orElseThrow(()-> new ResourceNotFoundException("Program id " + program.getProgramid() + " not found!"));
            newProgram.setProgramid(program.getProgramid());
        }

        newProgram.setName(program.getName());
        newProgram.getClubs().clear();
        for(Club c : program.getClubs()){
            newProgram.getClubs().add(new Club(c.getClubId(),newProgram));
        }
        return programrepos.save(newProgram);
    }

    @Override
    public Program update(Program program, long id) {
        Program currentProgram = programrepos.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Program " + id + " not found!"));
        if(program.getName()!=null){
            currentProgram.setName(program.getName());
        }
        if(program.getClubs().size() > 0){
            for(ClubPrograms cp : program.getClubs()){
                Program newProgram = clubrepos.findById(cp.getClub().getClubid())
                    .orElseThrow(()-> new ResourceNotFoundException("Club id " + cp.getClub().getClubid() + " not found!"));
                currentProgram.getClubs().add(new ClubPrograms(currentProgram, newClub));
            }
        }
        return programrepos.save(currentProgram);
    }
}
