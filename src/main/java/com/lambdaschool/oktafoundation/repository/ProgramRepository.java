package com.lambdaschool.oktafoundation.repository;

import com.lambdaschool.oktafoundation.models.Program;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProgramRepository
    extends CrudRepository<Program, Long> {
    Program findByName(String name);
}
