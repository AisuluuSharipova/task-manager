package kg.alatoo.task_management.mappers;

import kg.alatoo.task_management.dtos.UserDTO;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;
    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();

        testUser = User.builder()
                .id(1L)
                .firstName("Aisuluu")
                .lastName("Zhoodarbek")
                .email("aisuluu@example.com")
                .username("Aisuluu.Zhoodarbek")
                .password("encodedpassword")
                .enabled(true)
                .role(Role.USER)
                .build();

        testUserDTO = new UserDTO(
                1L,
                "Aisuluu",
                "Zhoodarbek",
                "aisuluu@example.com"
        );
    }

    @Test
    void testToDTO() {
        UserDTO dto = userMapper.toDTO(testUser);

        assertNotNull(dto);
        assertEquals(testUser.getId(), dto.getId());
        assertEquals(testUser.getFirstName(), dto.getFirstName());
        assertEquals(testUser.getLastName(), dto.getLastName());
        assertEquals(testUser.getEmail(), dto.getEmail());
    }

    @Test
    void testToEntity() {
        User entity = userMapper.toEntity(testUserDTO);

        assertNotNull(entity);
        assertEquals(testUserDTO.getId(), entity.getId());
        assertEquals(testUserDTO.getFirstName(), entity.getFirstName());
        assertEquals(testUserDTO.getLastName(), entity.getLastName());
        assertEquals(testUserDTO.getEmail(), entity.getEmail());

        assertEquals(testUserDTO.getEmail(), entity.getUsername());

        // Убедиться, что роль установлена
        assertEquals(Role.USER, entity.getRole());

        assertEquals("", entity.getPassword());

        assertTrue(entity.isEnabled());
    }
}
