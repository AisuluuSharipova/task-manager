package kg.alatoo.task_management.dtos;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TaskDTOTest {

    private TaskDTO taskDTO;

    @BeforeEach
    void setUp() {
        taskDTO = new TaskDTO();
    }

    @AfterEach
    void tearDown() {
        taskDTO = null;
    }

    @Test
    void getId() {
        taskDTO.setId(1L);
        assertEquals(1L, taskDTO.getId(), "getId should return the correct value");
    }

    @Test
    void getTitle() {
        taskDTO.setTitle("Test Task");
        assertEquals("Test Task", taskDTO.getTitle(), "getTitle should return the correct value");
    }

    @Test
    void getDescription() {
        taskDTO.setDescription("Task description");
        assertEquals("Task description", taskDTO.getDescription(), "getDescription should return the correct value");
    }

    @Test
    void getStatus() {
        taskDTO.setStatus("IN_PROGRESS");
        assertEquals("IN_PROGRESS", taskDTO.getStatus(), "getStatus should return the correct value");
    }

    @Test
    void getLevel() {
        taskDTO.setLevel("HIGH");
        assertEquals("HIGH", taskDTO.getLevel(), "getLevel should return the correct value");
    }

    @Test
    void getCreationDate() {
        Date creationDate = new Date();
        taskDTO.setCreationDate(creationDate);
        assertEquals(creationDate, taskDTO.getCreationDate(), "getCreationDate should return the correct value");
    }

    @Test
    void getEndDate() {
        LocalDate endDate = LocalDate.now().plusDays(5);
        taskDTO.setEndDate(endDate);
        assertEquals(endDate, taskDTO.getEndDate(), "getEndDate should return the correct value");
    }

    @Test
    void getAssignedUserId() {
        taskDTO.setAssignedUserId(2L);
        assertEquals(2L, taskDTO.getAssignedUserId(), "getAssignedUserId should return the correct value");
    }

    @Test
    void setId() {
        taskDTO.setId(3L);
        assertEquals(3L, taskDTO.getId(), "setId should correctly assign value");
    }

    @Test
    void setTitle() {
        taskDTO.setTitle("New Task");
        assertEquals("New Task", taskDTO.getTitle(), "setTitle should correctly assign value");
    }

    @Test
    void setDescription() {
        taskDTO.setDescription("Updated Description");
        assertEquals("Updated Description", taskDTO.getDescription(), "setDescription should correctly assign value");
    }

    @Test
    void setStatus() {
        taskDTO.setStatus("COMPLETED");
        assertEquals("COMPLETED", taskDTO.getStatus(), "setStatus should correctly assign value");
    }

    @Test
    void setLevel() {
        taskDTO.setLevel("MEDIUM");
        assertEquals("MEDIUM", taskDTO.getLevel(), "setLevel should correctly assign value");
    }

    @Test
    void setCreationDate() {
        Date newDate = new Date();
        taskDTO.setCreationDate(newDate);
        assertEquals(newDate, taskDTO.getCreationDate(), "setCreationDate should correctly assign value");
    }

    @Test
    void setEndDate() {
        LocalDate newEndDate = LocalDate.now().plusDays(10);
        taskDTO.setEndDate(newEndDate);
        assertEquals(newEndDate, taskDTO.getEndDate(), "setEndDate should correctly assign value");
    }

    @Test
    void setAssignedUserId() {
        taskDTO.setAssignedUserId(5L);
        assertEquals(5L, taskDTO.getAssignedUserId(), "setAssignedUserId should correctly assign value");
    }
}
