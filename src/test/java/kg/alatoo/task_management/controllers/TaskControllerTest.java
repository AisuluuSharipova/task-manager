package kg.alatoo.task_management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kg.alatoo.task_management.dtos.TaskDTO;
import kg.alatoo.task_management.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TaskControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private ObjectMapper objectMapper;
    private TaskDTO sampleTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(taskController).build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // для поддержки LocalDate

        sampleTask = new TaskDTO(
                1L,
                "Task Title",
                "Task Description",
                "New",
                "High",
                LocalDate.of(2025, 5, 5), // ✅ заменили на LocalDate
                LocalDate.of(2025, 5, 10),
                2L
        );
    }

    @Test
    void getAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(List.of(sampleTask));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Task Title"));
    }

    @Test
    void getTaskById() throws Exception {
        when(taskService.getTaskById(1L)).thenReturn(sampleTask);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task Title"))
                .andExpect(jsonPath("$.description").value("Task Description"));
    }

    @Test
    void createTask() throws Exception {
        when(taskService.createTask(any(TaskDTO.class))).thenReturn(sampleTask);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Task Title"));
    }

    @Test
    void updateTask() throws Exception {
        when(taskService.updateTask(eq(1L), any(TaskDTO.class))).thenReturn(sampleTask);

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task Title"));
    }

    @Test
    void partiallyUpdateTask() throws Exception {
        Map<String, Object> updates = Map.of("status", "In Progress");

        when(taskService.partiallyUpdateTask(eq(1L), any(Map.class))).thenReturn(sampleTask);

        mockMvc.perform(patch("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Task Title"));
    }

    @Test
    void deleteTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Task deleted successfully."));
    }
}
