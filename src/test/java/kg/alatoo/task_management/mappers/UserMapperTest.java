package kg.alatoo.task_management.mappers;

import kg.alatoo.task_management.dtos.UserDTO;
import kg.alatoo.task_management.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void toDTO_shouldConvertUserToUserDTO() {
        User user = new User(1L, "Aiss", "Zhoodarbek", "aiss@gmail.com");

        UserDTO userDTO = userMapper.toDTO(user);

        assertNotNull(userDTO);
        assertEquals(user.getId(), userDTO.getId());
        assertEquals(user.getFirstName(), userDTO.getFirstName());
        assertEquals(user.getLastName(), userDTO.getLastName());
        assertEquals(user.getEmail(), userDTO.getEmail());
    }

    @Test
    void toEntity_shouldConvertUserDTOToUser() {
        UserDTO userDTO = new UserDTO(1L, "Aiss", "Zhoodarbek", "aiss@gmail.com");

        User user = userMapper.toEntity(userDTO);

        assertNotNull(user);
        assertEquals(userDTO.getId(), user.getId());
        assertEquals(userDTO.getFirstName(), user.getFirstName());
        assertEquals(userDTO.getLastName(), user.getLastName());
        assertEquals(userDTO.getEmail(), user.getEmail());
    }

    @Test
    void toDTO_shouldReturnNullWhenUserIsNull() {
        assertNull(userMapper.toDTO(null));
    }

    @Test
    void toEntity_shouldReturnNullWhenUserDTOIsNull() {
        assertNull(userMapper.toEntity(null));
    }
}
