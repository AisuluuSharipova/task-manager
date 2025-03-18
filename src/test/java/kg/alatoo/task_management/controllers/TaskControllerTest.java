package kg.alatoo.task_management.controllers;

import kg.alatoo.task_management.dtos.TaskDTO;
import kg.alatoo.task_management.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    private TaskDTO sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new TaskDTO(1L, "Test Task", "Task Description", "New", "High", new Date(), "2025-04-10", 1L);
    }

    @Test
    void getAllTasks() throws Exception {
        List<TaskDTO> tasks = Arrays.asList(sampleTask);
        Mockito.when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].description").value("Task Description"));
    }

    @Test
    void getTaskById() throws Exception {
        Mockito.when(taskService.getTaskById(1L)).thenReturn(sampleTask);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void createTask() throws Exception {
        Mockito.when(taskService.createTask(any(TaskDTO.class))).thenReturn(sampleTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Test Task\", \"description\": \"Task Description\", \"status\": \"New\", \"level\": \"High\", \"endDate\": \"2025-04-10\", \"assignedUserId\": 1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Task Description"));
    }

    @Test
    void updateTask() throws Exception {
        Mockito.when(taskService.updateTask(eq(1L), any(TaskDTO.class))).thenReturn(sampleTask);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\": \"Updated Task\", \"description\": \"Updated Description\", \"status\": \"In Progress\", \"level\": \"Medium\", \"endDate\": \"2025-04-20\", \"assignedUserId\": 1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));  // Should match the mock return
    }

    @Test
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully."));
    }
}
