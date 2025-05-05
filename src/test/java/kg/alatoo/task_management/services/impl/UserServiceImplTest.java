package kg.alatoo.task_management.services.impl;

import kg.alatoo.task_management.dtos.UserDTO;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.exceptions.NotFoundException;
import kg.alatoo.task_management.mappers.UserMapper;
import kg.alatoo.task_management.repositories.TaskRepository;
import kg.alatoo.task_management.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setFirstName("Aisuluu");
        user.setLastName("Zhoodarbek");
        user.setEmail("aisuluu@example.com");

        userDTO = new UserDTO(1L, "Aisuluu", "Zhoodarbek", "aisuluu@example.com");
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        List<UserDTO> result = userService.getAllUsers();
        assertEquals(1, result.size());
        assertEquals("Aisuluu", result.get(0).getFirstName());
    }

    @Test
    void testGetUserById_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals("Aisuluu", result.getFirstName());
    }

    @Test
    void testGetUserById_notFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(2L));
    }

    @Test
    void testCreateUser() {
        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.createUser(userDTO);
        assertEquals("Aisuluu", result.getFirstName());
    }

    @Test
    void testUpdateUser_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        UserDTO result = userService.updateUser(1L, userDTO);
        assertEquals("Aisuluu", result.getFirstName());
    }

    @Test
    void testUpdateUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(1L, userDTO));
    }

    @Test
    void testPartiallyUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        Map<String, Object> updates = Map.of("firstName", "NewName");
        UserDTO result = userService.partiallyUpdateUser(1L, updates);

        assertEquals("Aisuluu", result.getFirstName());
    }

    @Test
    void testDeleteUser_found() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        assertDoesNotThrow(() -> userService.deleteUser(1L));
    }

    @Test
    void testDeleteUser_notFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
    }
}
