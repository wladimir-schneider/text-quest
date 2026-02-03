package pantera.textquest.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QuestTest {
    @Test
    public void testQuestCreation() {
        Quest q = new Quest("Test Quest", List.of("Step 1", "Step 2"), "creator");
        assertEquals("Test Quest", q.getTitle());
        assertEquals(2, q.getSteps().size());
        assertEquals("creator", q.getCreator());
        assertNotNull(q.getId());
    }

    @Test
    public void testQuestEmptySteps() {
        Quest q = new Quest("Quest", null, "user");
        assertNotNull(q.getSteps());
        assertTrue(q.getSteps().isEmpty());
    }

    @Test
    public void testQuestSettersAndGetters() {
        Quest q = new Quest();
        q.setTitle("New Quest");
        q.setSteps(List.of("A", "B", "C"));
        q.setCreator("admin");
        
        assertEquals("New Quest", q.getTitle());
        assertEquals(3, q.getSteps().size());
        assertEquals("admin", q.getCreator());
    }

    @Test
    public void testQuestIdUniqueness() {
        Quest q1 = new Quest("Q1", List.of(), "user");
        Quest q2 = new Quest("Q2", List.of(), "user");
        assertNotEquals(q1.getId(), q2.getId());
    }
}
