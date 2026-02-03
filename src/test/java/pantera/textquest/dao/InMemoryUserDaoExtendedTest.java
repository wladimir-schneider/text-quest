package pantera.textquest.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pantera.textquest.model.Quest;
import pantera.textquest.model.Role;
import pantera.textquest.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserDaoExtendedTest {
    private InMemoryUserDao dao;

    @BeforeEach
    public void setup() {
        dao = new InMemoryUserDao();
    }

    @Test
    public void testDefaultAdminExists() {
        assertTrue(dao.findByUsername("admin").isPresent());
        User admin = dao.findByUsername("admin").get();
        assertEquals(Role.ADMIN, admin.getRole());
    }

    @Test
    public void testAddMultipleUsers() {
        User u1 = new User();
        u1.setUsername("user1");
        u1.setPassword("pass1");
        
        User u2 = new User();
        u2.setUsername("user2");
        u2.setPassword("pass2");
        
        assertTrue(dao.add(u1));
        assertTrue(dao.add(u2));
        assertEquals(3, dao.listAll().size()); // admin + 2 users
    }

    @Test
    public void testAddDuplicateUserFails() {
        User u1 = new User();
        u1.setUsername("duplicate");
        u1.setPassword("pass");
        
        assertTrue(dao.add(u1));
        
        User u2 = new User();
        u2.setUsername("duplicate");
        u2.setPassword("other");
        
        assertFalse(dao.add(u2));
    }

    @Test
    public void testAddNullUserFails() {
        assertFalse(dao.add(null));
    }

    @Test
    public void testDeleteUser() {
        User u = new User();
        u.setUsername("todelete");
        u.setPassword("pass");
        dao.add(u);
        
        assertTrue(dao.findByUsername("todelete").isPresent());
        assertTrue(dao.delete("todelete"));
        assertFalse(dao.findByUsername("todelete").isPresent());
    }

    @Test
    public void testDeleteNonexistentUserFails() {
        assertFalse(dao.delete("nonexistent"));
    }

    @Test
    public void testAuthenticateSuccess() {
        User u = new User();
        u.setUsername("user");
        u.setPassword("secret");
        dao.add(u);
        
        assertTrue(dao.authenticate("user", "secret").isPresent());
    }

    @Test
    public void testAuthenticateWrongPassword() {
        User u = new User();
        u.setUsername("user");
        u.setPassword("secret");
        dao.add(u);
        
        assertFalse(dao.authenticate("user", "wrong").isPresent());
    }

    @Test
    public void testListAllUsers() {
        List<User> users = dao.listAll();
        assertEquals(1, users.size()); // admin
        
        User u = new User();
        u.setUsername("newuser");
        dao.add(u);
        
        assertEquals(2, dao.listAll().size());
    }

    @Test
    public void testAddAndListQuests() {
        User u = new User();
        u.setUsername("questcreator");
        dao.add(u);
        
        Quest q1 = new Quest("Quest 1", List.of("Step 1"), "questcreator");
        Quest q2 = new Quest("Quest 2", List.of("A", "B"), "questcreator");
        
        assertTrue(dao.addQuest("questcreator", q1));
        assertTrue(dao.addQuest("questcreator", q2));
        
        List<Quest> quests = dao.listQuests("questcreator");
        assertEquals(2, quests.size());
    }

    @Test
    public void testListQuestsEmptyUser() {
        List<Quest> quests = dao.listQuests("nonexistent");
        assertNotNull(quests);
        assertTrue(quests.isEmpty());
    }

    @Test
    public void testAddQuestNullFails() {
        assertFalse(dao.addQuest("user", null));
    }

    @Test
    public void testAddQuestNullUsernameFails() {
        Quest q = new Quest("Q", List.of(), "user");
        assertFalse(dao.addQuest(null, q));
    }
}
