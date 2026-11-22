package by.coldynee.homework_2.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void constructor_shouldSetCreatedAtAutomatically() {
        User user = new User("Никита", "test@test.com", 22);

        assertNotNull(user.getCreatedAt());
    }

    @Test
    void toString_shouldContainAllFields() {
        User user = new User("Никита", "test@test.com", 22);

        String result = user.toString();

        assertTrue(result.contains("Никита"));
        assertTrue(result.contains("test@test.com"));
        assertTrue(result.contains("22"));
    }

    @Test
    void settersAndGetters_shouldWork() {
        User user = new User();
        user.setName("Иван");
        user.setEmail("ivan@test.com");
        user.setAge(30);

        assertEquals("Иван", user.getName());
        assertEquals("ivan@test.com", user.getEmail());
        assertEquals(30, user.getAge());
    }
}