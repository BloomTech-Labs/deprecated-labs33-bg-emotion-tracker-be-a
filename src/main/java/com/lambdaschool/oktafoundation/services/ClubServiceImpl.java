package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.models.Club;
import com.lambdaschool.oktafoundation.repository.ClubRepository;
import org.apache.catalina.LifecycleState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service(value = "clubService")
public class ClubServiceImpl implements ClubService
{
    @Autowired
    ClubRepository clubRepository;

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
    public Club save(Club club)
    {
        Club newClub = new Club();
        return club;
    }

}
