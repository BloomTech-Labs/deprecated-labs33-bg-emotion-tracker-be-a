package com.lambdaschool.oktafoundation.services;

import com.lambdaschool.oktafoundation.OktaFoundationApplicationTest;
import com.lambdaschool.oktafoundation.exceptions.ResourceNotFoundException;
import com.lambdaschool.oktafoundation.models.Role;
import com.lambdaschool.oktafoundation.models.User;
import com.lambdaschool.oktafoundation.models.UserRoles;
import com.lambdaschool.oktafoundation.models.Useremail;
import com.lambdaschool.oktafoundation.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * This test class covers 100% of the methods and 100% of the lines in the UserServiceImpl.class
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OktaFoundationApplicationTest.class,
    properties = {
        "command.line.runner.enabled=false"})
public class UserServiceImplUnitTestNoDB
{
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userrepos;

    @MockBean
    private RoleService roleService;

    @MockBean
    HelperFunctions helperFunctions;

    private List<User> userList;


    @Before
    public void setUp()
    {
        userList = new ArrayList<>();

        Role r1 = new Role("superadmin");
        r1.setRoleid(1);
        Role r2 = new Role("clubdir");
        r2.setRoleid(2);
        Role r3 = new Role("ydp");
        r3.setRoleid(3);
        Role r4 = new Role("user");
        r4.setRoleid(4);


        // Test User 1 Super Admin
        User u1 = new User("llama001@maildrop.cc");
        u1.getRoles()
            .add(new UserRoles(u1,
                r1));

        u1.getUseremails()
            .add(new Useremail(u1,
                "llama001@maildrop.cc"));
        u1.getUseremails()
            .get(0)
            .setUseremailid(10);

        u1.setUserid(101);
        userList.add(u1);


        // Test User 2 Club Director
        User u2 = new User("llama002@maildrop.cc");
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));
        u2.getUseremails()
            .add(new Useremail(u2,
                "llama002@maildrop.cc"));
        u2.getUseremails()
            .get(0)
            .setUseremailid(12);

        u2.setUserid(102);
        userList.add(u2);

        // Test User 5 Youth Development Professional
        User u5 = new User("llama005@maildrop.cc");
        u5.getRoles()
            .add(new UserRoles(u5,
                r3));
        u5.getUseremails()
            .add(new Useremail(u5,
                "llama005@maildrop.cc"));
        u5.getUseremails()
            .get(0)
            .setUseremailid(15);

        u5.setUserid(105);
        userList.add(u5);


        // Test User 7 User role
        User u7 = new User("llama007@maildrop.cc");
        u7.getRoles()
            .add(new UserRoles(u7,
                r3));
        u7.getUseremails()
            .add(new Useremail(u7,
                "llama007@maildrop.cc"));
        u7.getUseremails()
            .get(0)
            .setUseremailid(17);

        u7.setUserid(107);
        userList.add(u7);

        System.out.println("\n*** Seed Data ***");
        for (User u : userList)
        {
            System.out.println(u.getUserid() + " " + u.getUsername());
        }
        System.out.println("*** Seed Data ***\n");

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown()
    {
    }

    @Test
    public void findUserById()
    {
        Mockito.when(userrepos.findById(101L))
            .thenReturn(Optional.of(userList.get(0)));

        assertEquals("llama001@maildrop.cc",
            userService.findUserById(101L)
                .getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findUserByIdNotFound()
    {
        Mockito.when(userrepos.findById(10L))
            .thenReturn(Optional.empty());

        assertEquals("llama001@maildrop.cc",
            userService.findUserById(10L)
                .getUsername());
    }

    @Test
    public void findAll()
    {
        Mockito.when(userrepos.findAll())
            .thenReturn(userList);

        assertEquals(4,
            userService.findAll()
                .size());
    }

    @Test
    public void delete()
    {
        Mockito.when(userrepos.findById(101L))
            .thenReturn(Optional.of(userList.get(0)));

        Mockito.doNothing()
            .when(userrepos)
            .deleteById(101L);

        userService.delete(101L);
        assertEquals(4,
            userList.size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void notFoundDelete()
    {
        Mockito.when(userrepos.findById(999L))
            .thenReturn(Optional.empty());

        Mockito.doNothing()
            .when(userrepos)
            .deleteById(999L);

        userService.delete(999L);
        assertEquals(4,
            userList.size());
    }

    @Test
    public void findByUsername()
    {
        Mockito.when(userrepos.findByUsername("llama001@maildrop.cc"))
            .thenReturn(userList.get(0));

        assertEquals("llama001@maildrop.cc",
            userService.findByName("llama001@maildrop.cc")
                .getUsername());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findByUsernameNotfound()
    {
        Mockito.when(userrepos.findByUsername("notauser"))
            .thenReturn(null);

        assertEquals("notauser",
            userService.findByName("notauser")
                .getUsername());
    }

    @Test
    public void findByNameContaining()
    {
        Mockito.when(userrepos.findByUsernameContainingIgnoreCase("llama"))
            .thenReturn(userList);

        assertEquals(4,
            userService.findByNameContaining("llama")
                .size());
    }

    @Test
    public void save()
    {
        Role r2 = new Role("clubdir");
        r2.setRoleid(2);

        User u2 = new User("llama002@maildrop.cc");
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));
        u2.getUseremails()
            .add(new Useremail(u2,
                "llama002@maildrop.cc"));

        Mockito.when(roleService.findRoleById(2))
            .thenReturn(r2);

        Mockito.when(userrepos.save(any(User.class)))
            .thenReturn(u2);

        assertEquals("llama002@maildrop.cc",
            userService.save(u2)
                .getUsername());
    }

    @Test
    public void savePut()
    {
        Role r3 = new Role("ydp");
        r3.setRoleid(3);

        User u5 = new User("llama005@maildrop.cc");
        u5.getRoles()
            .add(new UserRoles(u5,
                r3));
        u5.getUseremails()
            .add(new Useremail(u5,
                "llama005@maildrop.cc"));
        u5.setUserid(105L);

        Mockito.when(userrepos.findById(105L))
            .thenReturn(Optional.of(u5));

        Mockito.when(roleService.findRoleById(3))
            .thenReturn(r3);

        Mockito.when(userrepos.save(any(User.class)))
            .thenReturn(u5);

        assertEquals(105L,
            userService.save(u5)
                .getUserid());
    }

    @Test
    public void update()
    {
        Role r4 = new Role("user");
        r4.setRoleid(4);

        User u7 = new User("llama007@maildrop.cc");
        u7.getRoles()
            .add(new UserRoles(u7,
                r4));

        u7.getUseremails()
            .add(new Useremail(u7,
                "llama007@maildrop.cc"));


        Mockito.when(userrepos.findById(107L))
            .thenReturn(Optional.of(userList.get(3)));

        Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString()))
            .thenReturn(true);

        Mockito.when(userrepos.save(any(User.class)))
            .thenReturn(u7);

        Mockito.when(roleService.findRoleById(4))
            .thenReturn(r4);

        assertEquals("llama007@maildrop.cc",
            userService.update(u7,
                107L)
                .getUseremails()
                .get(0)
                .getUseremail());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateNotFound()
    {
        Role r2 = new Role("clubdir");
        r2.setRoleid(2);

        User u2 = new User("llama002@maildrop.cc");
        u2.getRoles()
            .add(new UserRoles(u2,
                r2));

        u2.getUseremails()
            .add(new Useremail(u2,
                "lama002@maildrop.cc"));

        Mockito.when(userrepos.findById(103L))
            .thenReturn(Optional.empty());

        Mockito.when(roleService.findRoleById(2))
            .thenReturn(r2);

        Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString()))
            .thenReturn(true);

        Mockito.when(userrepos.save(any(User.class)))
            .thenReturn(u2);

        assertEquals("llama002@maildrop.cc",
            userService.update(u2,
                103L)
                .getUseremails()
                .get(0)
                .getUseremail());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updateAuthorizedToMakeChange()
    {
        Role r4 = new Role("user");
        r4.setRoleid(4);

        User u7 = new User("llama007@maildrop.cc");
        u7.getRoles()
            .add(new UserRoles(u7,
                r4));

        u7.getUseremails()
            .add(new Useremail(u7,
                "llama007@maildrop.cc"));

        Mockito.when(roleService.findRoleById(4))
            .thenReturn(r4);

        Mockito.when(userrepos.findById(107L))
            .thenReturn(Optional.of(u7));

        Mockito.when(helperFunctions.isAuthorizedToMakeChange(anyString()))
            .thenReturn(false);

        Mockito.when(userrepos.save(any(User.class)))
            .thenReturn(u7);

        assertEquals("llama007@maildrop.cc",
            userService.update(u7,
                107L)
                .getUseremails()
                .get(0)
                .getUseremail());
    }

    @Test
    public void deleteAll()
    {
        Mockito.doNothing()
            .when(userrepos)
            .deleteAll();

        userService.deleteAll();
        assertEquals(4,
            userList.size());

    }
}