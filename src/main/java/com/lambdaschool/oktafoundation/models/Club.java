package com.lambdaschool.oktafoundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private String clubdirector;

    public Club()
    {
    }

    public Club(@NotNull String clubname, String clubdirector)
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

    public String getClubdirector() {
        return clubdirector;
    }

    public void setClubdirector(String clubdirector) {
        this.clubdirector = clubdirector;
    }

}