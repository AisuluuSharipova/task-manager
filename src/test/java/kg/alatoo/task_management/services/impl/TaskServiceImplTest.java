package kg.alatoo.task_management.services.impl;

import kg.alatoo.task_management.dtos.TaskDTO;
import kg.alatoo.task_management.entities.Task;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.exceptions.NotFoundException;
import kg.alatoo.task_management.mappers.TaskMapper;
import kg.alatoo.task_management.repositories.TaskRepository;
import kg.alatoo.task_management.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock private TaskRepository taskRepository;
    @Mock private UserRepository userRepository;
    @Mock private TaskMapper taskMapper;

    @InjectMocks private TaskServiceImpl taskService;

    private User user;
    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        task = new Task(1L, "Test Task", "Test Desc", "New", "High",
                LocalDate.now(), LocalDate.now().plusDays(1), user);

        taskDTO = new TaskDTO(1L, "Test Task", "Test Desc", "New", "High",
                LocalDate.now(), LocalDate.now().plusDays(1), 1L);
    }

    @Test
    void getAllTasks_shouldReturnList() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);

        List<TaskDTO> result = taskService.getAllTasks();

        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).getTitle());
    }

    @Test
    void getTaskById_shouldReturnTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.getTaskById(1L);

        assertEquals("Test Task", result.getTitle());
    }

    @Test
    void getTaskById_shouldThrowNotFound() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.getTaskById(2L));
    }

    @Test
    void createTask_shouldSaveAndReturn() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.createTask(taskDTO);

        assertEquals("Test Task", result.getTitle());
    }

    @Test
    void createTask_userNotFound_shouldThrow() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.createTask(taskDTO));
    }

    @Test
    void updateTask_shouldUpdateAndReturn() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.updateTask(1L, taskDTO);

        assertEquals("Test Task", result.getTitle());
    }

    @Test
    void updateTask_notFound_shouldThrow() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> taskService.updateTask(1L, taskDTO));
    }

    @Test
    void partiallyUpdateTask_shouldUpdateFields() {
        Map<String, Object> updates = Map.of("title", "Updated Task", "status", "In Progress");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskMapper.toDTO(task)).thenReturn(taskDTO);

        TaskDTO result = taskService.partiallyUpdateTask(1L, updates);

        assertNotNull(result);
    }

    @Test
    void deleteTask_shouldDelete() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_notFound_shouldThrow() {
        when(taskRepository.existsById(2L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> taskService.deleteTask(2L));
    }
}
