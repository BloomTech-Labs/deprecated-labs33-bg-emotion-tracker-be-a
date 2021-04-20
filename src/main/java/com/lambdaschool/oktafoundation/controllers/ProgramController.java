package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.services.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/programs")
public class ProgramController {
    @Autowired
    ProgramService programService;

    /**
     * List of all Programs
     * @return JSON List of all the programs
     * @see ProgramService#findAll()
     */
    @GetMapping(value ="/programs",
        produces = "application/json")
    public ResponseEntity<?> listPrograms(){
        List<Program> allPrograms = programService.findAll();
        return new ResponseEntity<>(allPrograms, HttpStatus.OK);
    }

    /**
     * The Program referenced by the given primary key
     * @param programId The primary key (long) of the program you seek
     * @return JSON object of the program you seek
     * @see ProgramService#findProgramById(long) ProgramService.findProgramById(long programId)
     */
    @GetMapping(value = "/program/{programid}",
        produces = "application/json")
    public ResponseEntity<?> getProgramById(@PathVariable Long programId){
        Program p = programService.findProgramById(programId);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    /**
     * The Program with the given name
     * @param programName The name of the program you seek
     * @return JSON object of the program you seek
     * @see ProgramService#findByName(String) ProgramService.findByName(String programName)
     */
    @GetMapping(value = "/program/name/{programname}",
        produces = "applcation/json")
    public ResponseEntity<?> getProgramByName(@PathVariable String programName){
        Program p = programService.findByName(programName);
        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    /**
     * Given a complete Program object, create a new Program record
     * @param newProgram
     * @return A location header with the URI to the newly created program
     * @see ProgramService#save(Program) ProgramService.save(Program)
     */
    @PostMapping(value = "program",
        consumes = "application/json")
    public ResponseEntity<?> addNewProgram(
        @Valid
        @RequestBody
            Program newProgram){
        newProgram.setProgramid(0);
        newProgram = programService.save(newProgram);
        //set the location header for the newly created resource
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newProgramURI = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{progamid}")
            .buildAndExpand(newProgram.getProgramid())
            .toUri();
        responseHeaders.setLocation(newProgramURI);

        return new ResponseEntity<>(null, responseHeaders, HttpStatus.CREATED);
    }

    /**
     * Allows you to update a program name only
     * @param programid The primary key of the program you wish to change
     * @param newProgram The new name(String) for the program
     * @return Status OK
     */
    @PutMapping(value = "/program/{programid}",
        consumes = {"application/json"})
    public ResponseEntity<?> updateProgram(
        @PathVariable
            long programid,
        @Valid
        @RequestBody
            Program newProgram){
        newProgram = programService.update(programid, newProgram);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
