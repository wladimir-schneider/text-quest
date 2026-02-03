package pantera.textquest.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    public void testUserCreation() {
        User u = new User();
        u.setUsername("testuser");
        u.setPassword("password");
        u.setRole(Role.USER);
        
        assertEquals("testuser", u.getUsername());
        assertEquals("password", u.getPassword());
        assertEquals(Role.USER, u.getRole());
        assertNotNull(u.getId());
    }

    @Test
    public void testUserProgress() {
        User u = new User();
        assertEquals("start", u.getProgress());
        
        u.setProgress("zone");
        assertEquals("zone", u.getProgress());
        
        u.setProgress("artifact_found");
        assertEquals("artifact_found", u.getProgress());
    }

    @Test
    public void testUserAdmin() {
        User u = new User();
        u.setRole(Role.ADMIN);
        assertEquals(Role.ADMIN, u.getRole());
    }

    @Test
    public void testUserIdUniqueness() {
        User u1 = new User();
        User u2 = new User();
        assertNotEquals(u1.getId(), u2.getId());
    }
}
