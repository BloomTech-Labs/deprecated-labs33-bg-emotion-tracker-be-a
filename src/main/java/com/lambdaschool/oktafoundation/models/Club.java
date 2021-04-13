package com.lambdaschool.oktafoundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clubs")
public class Club extends Auditable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long clubid;

    @NotNull
    @Column(unique = true)
    private String clubname;

    private int clubdirector;


    public Club()
    {
    }

    public Club(@NotNull String clubname, int clubdirector)
    {
        this.clubname = clubname;
        this.clubdirector = clubdirector;
    }

    public long getClubid() { return clubid; }

    public void setClubid(long clubid) { this.clubid = clubid; }

    public String getClubname() {
        return clubname;
    }

    public void setClubname(String clubname) {
        this.clubname = clubname;
    }

    public int getClubdirector() { return clubdirector; }

    public void setClubdirector(int clubdirector) { this.clubdirector = clubdirector; }
}
