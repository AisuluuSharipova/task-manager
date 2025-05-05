package kg.alatoo.task_management.mappers;

import kg.alatoo.task_management.dtos.TaskDTO;
import kg.alatoo.task_management.entities.Task;
import kg.alatoo.task_management.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {

    private TaskMapper taskMapper;
    private Task task;
    private TaskDTO taskDTO;
    private User assignedUser;

    @BeforeEach
    void setUp() {
        taskMapper = new TaskMapper();

        assignedUser = new User();
        assignedUser.setId(1L);

        task = new Task(
                10L,
                "Title",
                "Description",
                "New",
                "High",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                assignedUser
        );

        taskDTO = new TaskDTO(
                10L,
                "Title",
                "Description",
                "New",
                "High",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                1L
        );
    }

    @Test
    void testToDTO() {
        TaskDTO dto = taskMapper.toDTO(task);
        assertNotNull(dto);
        assertEquals(task.getId(), dto.getId());
        assertEquals(task.getTitle(), dto.getTitle());
        assertEquals(task.getAssignedUser().getId(), dto.getAssignedUserId());
    }

    @Test
    void testToEntity() {
        Task entity = taskMapper.toEntity(taskDTO, assignedUser);
        assertNotNull(entity);
        assertEquals(taskDTO.getTitle(), entity.getTitle());
        assertEquals(assignedUser, entity.getAssignedUser());
    }

    @Test
    void testToDTO_NullTask() {
        assertNull(taskMapper.toDTO(null));
    }

    @Test
    void testToEntity_NullDTO() {
        assertNull(taskMapper.toEntity(null, assignedUser));
    }
}
