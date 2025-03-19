package kg.alatoo.task_management.mappers;

import kg.alatoo.task_management.dtos.TaskDTO;
import kg.alatoo.task_management.entities.Task;
import kg.alatoo.task_management.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private TaskMapper taskMapper;
    private Task testTask;
    private TaskDTO testTaskDTO;
    private User testUser;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();

        testUser = new User(1L, "Aisuluu", "Zhoodarbek", "aisuluu@example.com");

        testTask = new Task(
                1L, "Task Title", "Task Description", "New", "High",
                new Date(System.currentTimeMillis()), Date.valueOf(LocalDate.of(2025, 4, 10)), testUser
        );

        testTaskDTO = new TaskDTO(
                1L, "Task Title", "Task Description", "New", "High",
                new Date(System.currentTimeMillis()), LocalDate.of(2025, 4, 10), 1L
        );
    }

    @Test
    void toDTO_validTask_returnsCorrectDTO() {
        TaskDTO result = taskMapper.toDTO(testTask);

        assertNotNull(result);
        assertEquals(testTask.getId(), result.getId());
        assertEquals(testTask.getTitle(), result.getTitle());
        assertEquals(testTask.getDescription(), result.getDescription());
        assertEquals(testTask.getStatus(), result.getStatus());
        assertEquals(testTask.getLevel(), result.getLevel());
        assertEquals(testTask.getEndDate().toLocalDate(), result.getEndDate());
        assertEquals(testTask.getAssignedUser().getId(), result.getAssignedUserId());
    }

    @Test
    void toDTO_nullTask_returnsNull() {
        assertNull(taskMapper.toDTO(null));
    }

    @Test
    void toEntity_validDTO_returnsCorrectTask() {
        Task result = taskMapper.toEntity(testTaskDTO, testUser);

        assertNotNull(result);
        assertEquals(testTaskDTO.getId(), result.getId());
        assertEquals(testTaskDTO.getTitle(), result.getTitle());
        assertEquals(testTaskDTO.getDescription(), result.getDescription());
        assertEquals(testTaskDTO.getStatus(), result.getStatus());
        assertEquals(testTaskDTO.getLevel(), result.getLevel());
        assertEquals(Date.valueOf(testTaskDTO.getEndDate()), result.getEndDate());
        assertEquals(testUser, result.getAssignedUser());
    }

    @Test
    void toEntity_nullDTO_returnsNull() {
        assertNull(taskMapper.toEntity(null, testUser));
    }
}
