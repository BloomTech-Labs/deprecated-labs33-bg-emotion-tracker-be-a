package com.lambdaschool.oktafoundation;

import com.lambdaschool.oktafoundation.models.Program;
import com.lambdaschool.oktafoundation.models.Role;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.models.UserRoles;
import com.lambdaschool.oktafoundation.services.ProgramService;
import com.lambdaschool.oktafoundation.services.RoleService;
import com.lambdaschool.oktafoundation.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * SeedData puts both known and random data into the database. It implements CommandLineRunner.
 * <p>
 * CoomandLineRunner: Spring Boot automatically runs the run method once and only once
 * after the application context has been loaded.
 */
@Transactional
@ConditionalOnProperty(
    prefix = "command.line.runner",
    value = "enabled",
    havingValue = "true",
    matchIfMissing = true)
@Component
public class SeedData
    implements CommandLineRunner
{
    /**
     * Connects the Role Service to this process
     */
    @Autowired
    RoleService roleService;

    /**
     * Connects the user service to this process
     */
    @Autowired
    UserService userService;

    @Autowired
    ProgramService programService;

    /**
     * Generates test, seed data for our application
     * First a set of known data is seeded into our database.
     * Second a random set of data using Java Faker is seeded into our database.
     * Note this process does not remove data from the database. So if data exists in the database
     * prior to running this process, that data remains in the database.
     *
     * @param args The parameter is required by the parent interface but is not used in this process.
     */
    @Transactional
    @Override
    public void run(String[] args) throws
                                   Exception
    {
        userService.deleteAll();
        roleService.deleteAll();
        programService.deleteAll();
        Role r1 = new Role("superadmin");
        Role r2 = new Role("clubdir");
        Role r3 = new Role("ydp");
        Role r4 = new Role("user");

        r1 = roleService.save(r1);
        r2 = roleService.save(r2);
        r3 = roleService.save(r3);
        r4 = roleService.save(r4);

        Program p1 = new Program("Football");
        Program p2 = new Program("BasketBall");
        Program p3 = new Program("BaseBall");

        p1 = programService.save(p1);
        p2 = programService.save(p2);
        p3 = programService.save(p3);

        // Super Admin
        User u1 = new User("llama001@maildrop.cc");
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));
        userService.save(u1);


        // Club Directors
        User u2 = new User("llama002@maildrop.cc");
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));
        userService.save(u2);

        User u3 = new User("llama003@maildrop.cc");
        u3.getRoles()
            .add(new UserRoles(u3,
                r2));
        userService.save(u3);

        User u4 = new User("llama004@maildrop.cc");
        u4.getRoles()
            .add(new UserRoles(u4,
                r2));
        userService.save(u4);

        // Youth Development Professionals
        User u5 = new User("llama005@maildrop.cc");
        u5.getRoles()
            .add(new UserRoles(u5,
                r3));
        userService.save(u5);

        User u6 = new User("llama006@maildrop.cc");
        u6.getRoles()
            .add(new UserRoles(u6,
                r3));
        userService.save(u6);

        User u7 = new User("llama007@maildrop.cc");
        u7.getRoles()
            .add(new UserRoles(u7,
                r4));
        userService.save(u7);
    }
}