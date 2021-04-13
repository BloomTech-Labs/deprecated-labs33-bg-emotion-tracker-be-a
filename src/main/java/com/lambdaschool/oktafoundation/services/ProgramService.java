package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Program;

import java.util.List;

public interface ProgramService {
    List<Program> findAll();

    List<Program> findByClubId(long clubId);

    Program findProgramById(long id);

    Program findProgramByName(String name);

    void delete(long id);

    Program save(Program program);

    Program update(Program program, long id);

}
