package kg.alatoo.task_management.dtos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO();
    }

    @AfterEach
    void tearDown() {
        userDTO = null;
    }

    @Test
    void getId() {
        userDTO.setId(1L);
        assertEquals(1L, userDTO.getId(), "getId should return the correct value");
    }

    @Test
    void getFirstName() {
        userDTO.setFirstName("John");
        assertEquals("John", userDTO.getFirstName(), "getFirstName should return the correct value");
    }

    @Test
    void getLastName() {
        userDTO.setLastName("Doe");
        assertEquals("Doe", userDTO.getLastName(), "getLastName should return the correct value");
    }

    @Test
    void getEmail() {
        userDTO.setEmail("john.doe@example.com");
        assertEquals("john.doe@example.com", userDTO.getEmail(), "getEmail should return the correct value");
    }

    @Test
    void setId() {
        userDTO.setId(2L);
        assertEquals(2L, userDTO.getId(), "setId should correctly assign value");
    }

    @Test
    void setFirstName() {
        userDTO.setFirstName("Alice");
        assertEquals("Alice", userDTO.getFirstName(), "setFirstName should correctly assign value");
    }

    @Test
    void setLastName() {
        userDTO.setLastName("Smith");
        assertEquals("Smith", userDTO.getLastName(), "setLastName should correctly assign value");
    }

    @Test
    void setEmail() {
        userDTO.setEmail("alice.smith@example.com");
        assertEquals("alice.smith@example.com", userDTO.getEmail(), "setEmail should correctly assign value");
    }
}
