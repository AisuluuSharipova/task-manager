package kg.alatoo.task_management.services.impl;

import kg.alatoo.task_management.dtos.UserDTO;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.mappers.UserMapper;
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
    void getAllUsers_ShouldReturnListOfUsers() {
        when(userRepository.findAll()).thenReturn(List.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        List<UserDTO> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("Aisuluu", users.get(0).getFirstName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        UserDTO userDTO = userService.getUserById(1L);

        assertEquals("Aisuluu", userDTO.getFirstName());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(2L));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserExists() {
        when(userRepository.findByEmail("aisuluu@example.com")).thenReturn(Optional.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        UserDTO userDTO = userService.getUserByEmail("aisuluu@example.com");

        assertEquals("aisuluu@example.com", userDTO.getEmail());
        verify(userRepository, times(1)).findByEmail("aisuluu@example.com");
    }

    @Test
    void getUserByEmail_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserByEmail("unknown@example.com"));

        assertEquals("User with email unknown@example.com not found", exception.getMessage());
    }

    @Test
    void getUsersByName_ShouldReturnUsers_WhenUsersExist() {
        when(userRepository.findByFirstNameAndLastName("Aisuluu", "Zhoodarbek")).thenReturn(List.of(testUser));
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        List<UserDTO> users = userService.getUsersByName("Aisuluu", "Zhoodarbek");

        assertEquals(1, users.size());
        assertEquals("Aisuluu", users.get(0).getFirstName());
    }

    @Test
    void createUser_ShouldReturnSavedUser() {
        when(userMapper.toEntity(testUserDTO)).thenReturn(testUser);
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        UserDTO savedUser = userService.createUser(testUserDTO);

        assertNotNull(savedUser);
        assertEquals("Aisuluu", savedUser.getFirstName());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(testUser)).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        UserDTO updatedUser = userService.updateUser(1L, testUserDTO);

        assertEquals("Aisuluu", updatedUser.getFirstName());
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void deleteUser_ShouldDeleteUser() {
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }
}
