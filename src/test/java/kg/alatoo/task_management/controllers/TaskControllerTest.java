package kg.alatoo.task_management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import kg.alatoo.task_management.dtos.TaskDTO;
import kg.alatoo.task_management.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;
    private TaskService taskService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        taskService = Mockito.mock(TaskService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new TaskController(taskService)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllTasks() throws Exception {
        List<TaskDTO> tasks = List.of(
                new TaskDTO(1L, "Task 1", "Description 1", "New", "High", new Date(), "2025-04-10", 1L),
                new TaskDTO(2L, "Task 2", "Description 2", "In Progress", "Medium", new Date(), "2025-04-15", 2L)
        );

        when(taskService.getAllTasks()).thenReturn(tasks);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    void getTaskById() throws Exception {
        TaskDTO task = new TaskDTO(1L, "Task 1", "Description 1", "New", "High", new Date(), "2025-04-10", 1L);

        when(taskService.getTaskById(1L)).thenReturn(task);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task 1"));
    }

    @Test
    void createTask() throws Exception {
        TaskDTO taskDTO = new TaskDTO(null, "New Task", "Description", "New", "Medium", new Date(), "2025-05-01", 1L);
        TaskDTO savedTask = new TaskDTO(1L, "New Task", "Description", "New", "Medium", new Date(), "2025-05-01", 1L);

        when(taskService.createTask(any())).thenReturn(savedTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("New Task"));
    }

    @Test
    void updateTask() throws Exception {
        TaskDTO updatedTask = new TaskDTO(1L, "Updated Task", "Updated Description", "In Progress", "High", new Date(), "2025-05-05", 1L);

        when(taskService.updateTask(eq(1L), any())).thenReturn(updatedTask);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"))
                .andExpect(jsonPath("$.status").value("In Progress"));
    }

    @Test
    void partiallyUpdateTask() throws Exception {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "Done");
        updates.put("level", "Low");

        TaskDTO updatedTask = new TaskDTO(1L, "Task 1", "Description 1", "Done", "Low", new Date(), "2025-04-10", 1L);

        when(taskService.partiallyUpdateTask(eq(1L), any())).thenReturn(updatedTask);

        mockMvc.perform(patch("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Done"))
                .andExpect(jsonPath("$.level").value("Low"));
    }

    @Test
    void deleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully."));
    }
}
