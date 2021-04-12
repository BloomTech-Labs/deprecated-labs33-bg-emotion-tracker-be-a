package com.lambdaschool.oktafoundation.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.mail.imap.protocol.ID;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Entity
@Table(name = "members")
public class Member extends Auditable
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(unique = true)
    private String memberid;

//    @NotNull
//    private String clubid;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "clubid")
    @JsonIgnoreProperties(value = "members",
            allowSetters = true)
    private Club club;

    public Member()
    {
    }

//    public Member(
//            @NotNull String memberid,
//            @NotNull String clubid)
//    {
//        this.memberid = memberid;
//        this.clubid = clubid;
//    }


    public Member(@NotNull String memberid, @NotNull Club club) {
        this.memberid = memberid;
        this.club = club;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getMemberid()
    {
        return memberid;
    }

    public void setMemberid(String memberid)
    {
        this.memberid = memberid;
    }

//    public String getClubid()
//    {
//        return clubid;
//    }
//
//    public void setClubid(String clubid)
//    {
//        this.clubid = clubid;
//    }


    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
    }
}