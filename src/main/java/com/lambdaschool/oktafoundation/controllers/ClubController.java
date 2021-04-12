package com.lambdaschool.oktafoundation.controllers;

import com.lambdaschool.oktafoundation.repository.ClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClubController
{
    @Autowired
    ClubRepository clubRepository;
}
