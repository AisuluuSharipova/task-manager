package kg.alatoo.task_management.services.impl;

import kg.alatoo.task_management.dtos.UserDTO;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.mappers.UserMapper;
import kg.alatoo.task_management.repositories.UserRepository;
import kg.alatoo.task_management.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        userMapper = Mockito.mock(UserMapper.class);
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    void getAllUsers() {
        List<User> users = List.of(
                new User(1L, "Aisuluu", "Sharipova", "aisuluu@example.com"),
                new User(2L, "Aisuluu", "Zhoodarbek", "zhoodarbek@example.com")
        );

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDTO(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
        });

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("Aisuluu", result.get(0).getFirstName());
        assertEquals("Sharipova", result.get(0).getLastName());
    }

    @Test
    void getUserById() {
        User user = new User(1L, "Aisuluu", "Sharipova", "aisuluu@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(new UserDTO(1L, "Aisuluu", "Sharipova", "aisuluu@example.com"));

        UserDTO result = userService.getUserById(1L);
        assertEquals("Aisuluu", result.getFirstName());
    }

    @Test
    void getUserByEmail() {
        User user = new User(1L, "Aisuluu", "Zhoodarbek", "aisuluu.zhoodarbek@example.com");

        when(userRepository.findByEmail("aisuluu.zhoodarbek@example.com")).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(new UserDTO(1L, "Aisuluu", "Zhoodarbek", "aisuluu.zhoodarbek@example.com"));

        UserDTO result = userService.getUserByEmail("aisuluu.zhoodarbek@example.com");

        assertEquals("Aisuluu", result.getFirstName());
        assertEquals("Zhoodarbek", result.getLastName());
    }

    @Test
    void getUsersByName() {
        List<User> users = List.of(new User(1L, "Aisuluu", "Sharipova", "aisuluu@example.com"));

        when(userRepository.findByFirstNameAndLastName("Aisuluu", "Sharipova")).thenReturn(users);
        when(userMapper.toDTO(any())).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
        });

        List<UserDTO> result = userService.getUsersByName("Aisuluu", "Sharipova");

        assertEquals(1, result.size());
        assertEquals("Aisuluu", result.get(0).getFirstName());
    }

    @Test
    void createUser() {
        UserDTO userDTO = new UserDTO(null, "Aisuluu", "Zhoodarbek", "aisuluu.zhoodarbek@example.com");
        User user = new User(1L, "Aisuluu", "Zhoodarbek", "aisuluu.zhoodarbek@example.com");

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(new UserDTO(1L, "Aisuluu", "Zhoodarbek", "aisuluu.zhoodarbek@example.com"));

        UserDTO result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals("Aisuluu", result.getFirstName());
    }

    @Test
    void updateUser() {
        User user = new User(1L, "Aisuluu", "Sharipova", "aisuluu@example.com");
        UserDTO updatedUserDTO = new UserDTO(1L, "Updated", "Sharipova", "updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(updatedUserDTO);

        UserDTO result = userService.updateUser(1L, updatedUserDTO);

        assertEquals("Updated", result.getFirstName());
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    void partiallyUpdateUser() {
        User user = new User(1L, "Aisuluu", "Sharipova", "aisuluu@example.com");
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", "Updated");
        updates.put("email", "updated@example.com");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(new UserDTO(1L, "Updated", "Sharipova", "updated@example.com"));

        UserDTO result = userService.partiallyUpdateUser(1L, updates);

        assertEquals("Updated", result.getFirstName());
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    void deleteUser() {
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
