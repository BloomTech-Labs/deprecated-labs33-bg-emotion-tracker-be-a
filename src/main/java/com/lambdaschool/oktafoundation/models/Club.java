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
    private long id;

    @NotNull
    @Column(unique = true)
    private String clubname;

    private String location;

    @OneToMany(mappedBy = "club",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonIgnoreProperties(value = "club",
            allowSetters = true)
    private List<Member> members = new ArrayList<>();

//    @OneToMany(mappedBy = "club",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true)
//    @JsonIgnoreProperties(value = "club",
//            allowSetters = true)
//    private List<Program> programs = new ArrayList<>();


    public Club()
    {
    }

    public Club(@NotNull String clubname, String location)
    {
        this.clubname = clubname;
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getClubname() {
        return clubname;
    }

    public void setClubname(String clubname) {
        this.clubname = clubname;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
