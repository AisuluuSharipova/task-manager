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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task testTask;
    private TaskDTO testTaskDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User(1L, "Aisuluu", "Zhoodarbek", "aisuluu@example.com");
        testTask = new Task(1L, "Task Title", "Task Description", "New", "High",
                new Date(System.currentTimeMillis()), Date.valueOf(LocalDate.of(2025, 4, 10)), testUser);
        testTaskDTO = new TaskDTO(1L, "Task Title", "Task Description", "New", "High",
                new Date(System.currentTimeMillis()), LocalDate.of(2025, 4, 10), 1L);
    }

    @Test
    void getAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(testTask));
        when(taskMapper.toDTO(testTask)).thenReturn(testTaskDTO);

        List<TaskDTO> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Task Title", result.get(0).getTitle());
    }

    @Test
    void getTaskById_found() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskMapper.toDTO(testTask)).thenReturn(testTaskDTO);

        TaskDTO result = taskService.getTaskById(1L);

        assertNotNull(result);
        assertEquals("Task Title", result.getTitle());
    }

    @Test
    void getTaskById_notFound() {
        when(taskRepository.findById(2L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> taskService.getTaskById(2L));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void createTask() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(taskMapper.toDTO(testTask)).thenReturn(testTaskDTO);

        TaskDTO result = taskService.createTask(testTaskDTO);

        assertNotNull(result);
        assertEquals("Task Title", result.getTitle());
    }

    @Test
    void updateTask_found() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);
        when(taskMapper.toDTO(testTask)).thenReturn(testTaskDTO);

        TaskDTO result = taskService.updateTask(1L, testTaskDTO);

        assertNotNull(result);
        assertEquals("Task Title", result.getTitle());
    }

    @Test
    void updateTask_notFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> taskService.updateTask(1L, testTaskDTO));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void partiallyUpdateTask_found() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0)); // ✅ Исправление
        when(taskMapper.toDTO(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            return new TaskDTO(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(),
                    task.getLevel(), task.getCreationDate(), task.getEndDate().toLocalDate(), task.getAssignedUser().getId());
        });

        Map<String, Object> updates = Map.of("title", "Updated Task Title");

        TaskDTO result = taskService.partiallyUpdateTask(1L, updates);

        assertNotNull(result);
        assertEquals("Updated Task Title", result.getTitle());
    }


    @Test
    void partiallyUpdateTask_notFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> taskService.partiallyUpdateTask(1L, Map.of("title", "New Title")));

        assertEquals("Task not found", exception.getMessage());
    }

    @Test
    void deleteTask_found() {
        when(taskRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taskRepository).deleteById(1L);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTask_notFound() {
        when(taskRepository.existsById(2L)).thenReturn(false);

        NotFoundException exception = assertThrows(NotFoundException.class, () -> taskService.deleteTask(2L));

        assertEquals("Task not found", exception.getMessage());
    }
}
