package pantera.textquest.dao;

import org.junit.jupiter.api.Test;
import pantera.textquest.model.User;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryUserDaoTest {
    @Test
    public void addAndFindUser() {
        InMemoryUserDao dao = new InMemoryUserDao();
        User u = new User();
        u.setUsername("testuser");
        u.setPassword("pwd");
        assertTrue(dao.add(u));
        assertTrue(dao.findByUsername("testuser").isPresent());
        assertTrue(dao.authenticate("testuser", "pwd").isPresent());
        assertFalse(dao.authenticate("testuser", "wrong").isPresent());
        assertTrue(dao.delete("testuser"));
        assertFalse(dao.findByUsername("testuser").isPresent());
    }
}
