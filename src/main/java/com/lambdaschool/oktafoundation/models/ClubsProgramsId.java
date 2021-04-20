package com.lambdaschool.oktafoundation.models;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ClubsProgramsId
    implements Serializable {
    private long club;

    private long program;

    public ClubsProgramsId() {
    }

    public long getClub() {
        return club;
    }

    public void setClub(long club) {
        this.club = club;
    }

    public long getProgram() {
        return program;
    }

    public void setProgram(long program) {
        this.program = program;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        // boolean temp = (o.getClass() instanceof Class);
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        ClubsProgramsId that = (ClubsProgramsId) o;
        return club == that.club &&
            program == that.program;
    }

    @Override
    public int hashCode()
    {
        return 7;
    }
}
