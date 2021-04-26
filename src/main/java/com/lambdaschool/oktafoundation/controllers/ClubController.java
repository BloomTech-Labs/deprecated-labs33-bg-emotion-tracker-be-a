package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.models.Club;
import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.repository.ClubRepository;
import com.lambdaschool.oktafoundation.services.ClubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/clubs")
public class ClubController
{
    @Autowired
    ClubRepository clubRepository;

    @Autowired
    private ClubService clubService;

    @GetMapping(value = "/clubs",
        produces = "application/json")
    public ResponseEntity<?> listAllClubs()
    {
        List<Club> myClubs = clubService.findAll();
        return new ResponseEntity<>(myClubs, HttpStatus.OK);
    }

    @PostMapping (value = "/upload", consumes = "multipart/form-data", produces = "application/json")
    public ResponseEntity<?> uploadClubs(
        MultipartFile csvfile) throws Exception {
        List<Club> addedClubs = clubService.saveNewClubs(csvfile.getInputStream());
        if (addedClubs.size() < 1) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(addedClubs, HttpStatus.CREATED);
        }
    }

}
