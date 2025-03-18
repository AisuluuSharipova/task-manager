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

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();
    }

    @Test
    void toDTO_shouldConvertTaskToTaskDTO() {
        User assignedUser = new User(2L, "Aiss", "Zhoodarbek", "aiss@gmail.com");
        Task task = new Task(
                1L,
                "Test Task",
                "Description",
                "NEW",
                "HIGH",
                Date.valueOf(LocalDate.now()),
                Date.valueOf(LocalDate.now().plusDays(7)),
                assignedUser
        );

        TaskDTO taskDTO = taskMapper.toDTO(task);

        assertNotNull(taskDTO);
        assertEquals(task.getId(), taskDTO.getId());
        assertEquals(task.getTitle(), taskDTO.getTitle());
        assertEquals(task.getDescription(), taskDTO.getDescription());
        assertEquals(task.getStatus(), taskDTO.getStatus());
        assertEquals(task.getLevel(), taskDTO.getLevel());
        assertEquals(task.getCreationDate(), taskDTO.getCreationDate());
        assertEquals(task.getEndDate().toString(), taskDTO.getEndDate());
        assertEquals(task.getAssignedUser().getId(), taskDTO.getAssignedUserId());
    }

    @Test
    void toEntity_shouldConvertTaskDTOToTask() {
        TaskDTO taskDTO = new TaskDTO(
                1L,
                "Test Task",
                "Description",
                "NEW",
                "HIGH",
                Date.valueOf(LocalDate.now()),
                LocalDate.now().plusDays(7).toString(),
                2L
        );

        User assignedUser = new User(2L, "Aiss", "Zhoodarbek", "aiss@gmail.com");

        Task task = taskMapper.toEntity(taskDTO, assignedUser);

        assertNotNull(task);
        assertEquals(taskDTO.getId(), task.getId());
        assertEquals(taskDTO.getTitle(), task.getTitle());
        assertEquals(taskDTO.getDescription(), task.getDescription());
        assertEquals(taskDTO.getStatus(), task.getStatus());
        assertEquals(taskDTO.getLevel(), task.getLevel());
        assertEquals(taskDTO.getCreationDate(), task.getCreationDate());
        assertEquals(Date.valueOf(taskDTO.getEndDate()), task.getEndDate());
        assertEquals(assignedUser, task.getAssignedUser());
    }
}
