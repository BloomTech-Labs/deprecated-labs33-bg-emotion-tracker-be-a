package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Club;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ClubService
{
    List<Club> findAll();

    Club save(Club club);

    List<Club> saveNewClubs(InputStream stream) throws IOException;

    public void deleteAll();
}
