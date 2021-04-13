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



    public Member()
    {
    }

    public Member(long id, @NotNull String memberid) {
        this.id = id;
        this.memberid = memberid;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }
}