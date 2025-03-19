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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User(1L, "Aisuluu", "Zhoodarbek", "aisuluu@example.com");
        testUserDTO = new UserDTO(1L, "Aisuluu", "Zhoodarbek", "aisuluu@example.com");
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        List<UserDTO> result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Aisuluu", result.get(0).getFirstName());
    }

    @Test
    void getUserById_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        UserDTO result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals("Aisuluu", result.getFirstName());
    }

    @Test
    void getUserById_notFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserById(2L));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getUserByEmail_found() {
        when(userRepository.findByEmail("aisuluu@example.com")).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        UserDTO result = userService.getUserByEmail("aisuluu@example.com");

        assertNotNull(result);
        assertEquals("Aisuluu", result.getFirstName());
    }

    @Test
    void getUserByEmail_notFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.getUserByEmail("unknown@example.com"));

        assertEquals("User with email unknown@example.com not found", exception.getMessage());
    }

    @Test
    void createUser() {
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);
        when(userMapper.toEntity(testUserDTO)).thenReturn(testUser);

        UserDTO result = userService.createUser(testUserDTO);

        assertNotNull(result);
        assertEquals("Aisuluu", result.getFirstName());
    }

    @Test
    void updateUser_found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        UserDTO result = userService.updateUser(1L, testUserDTO);

        assertNotNull(result);
        assertEquals("Aisuluu", result.getFirstName());
    }

    @Test
    void updateUser_notFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.updateUser(1L, testUserDTO));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteUser_found() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteUser_notFound() {
        when(userRepository.existsById(2L)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteUser(2L));

        assertEquals("User not found", exception.getMessage());
    }
}
