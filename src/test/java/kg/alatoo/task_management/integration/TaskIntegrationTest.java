package kg.alatoo.task_management.integration;

import kg.alatoo.task_management.dtos.TaskDTO;
import kg.alatoo.task_management.entities.Task;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.repositories.TaskRepository;
import kg.alatoo.task_management.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.sql.Date;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private User testUser;
    private Task testTask;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
        userRepository.deleteAll();

        testUser = userRepository.save(new User(null, "Aisuluu", "Sharipova", "aisuluu@gmail.com"));

        testTask = taskRepository.save(new Task(
                null, "Initial Task", "Initial Description", "New", "High",
                new Date(System.currentTimeMillis()), Date.valueOf(LocalDate.of(2025, 6, 10)), testUser));
    }

    @Test
    void testCreateTask() {
        TaskDTO taskRequest = new TaskDTO(
                null, "New Task", "Description of new task", "New", "Medium",
                new Date(System.currentTimeMillis()), LocalDate.of(2025, 6, 20), testUser.getId());

        ResponseEntity<TaskDTO> response = restTemplate.postForEntity(
                "/api/tasks", taskRequest, TaskDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("New Task");

        Task savedTask = taskRepository.findById(response.getBody().getId()).orElseThrow();
        assertThat(savedTask.getTitle()).isEqualTo("New Task");
    }

    @Test
    void testGetAllTasks() {
        ResponseEntity<TaskDTO[]> response = restTemplate.getForEntity("/api/tasks", TaskDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);
    }

    @Test
    void testGetTaskById() {
        ResponseEntity<TaskDTO> response = restTemplate.getForEntity("/api/tasks/" + testTask.getId(), TaskDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(testTask.getId());
    }

    @Test
    void testUpdateTask() {
        TaskDTO updatedTaskDTO = new TaskDTO(
                testTask.getId(), "Updated Task", "Updated Description", "In Progress", "High",
                testTask.getCreationDate(), LocalDate.of(2025, 7, 10), testUser.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<TaskDTO> requestEntity = new HttpEntity<>(updatedTaskDTO, headers);

        ResponseEntity<TaskDTO> response = restTemplate.exchange(
                "/api/tasks/" + testTask.getId(), HttpMethod.PUT, requestEntity, TaskDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTitle()).isEqualTo("Updated Task");

        Task updatedTask = taskRepository.findById(testTask.getId()).orElseThrow();
        assertThat(updatedTask.getTitle()).isEqualTo("Updated Task");
    }

    @Test
    void testDeleteTask() {
        restTemplate.delete("/api/tasks/" + testTask.getId());

        assertThat(taskRepository.findById(testTask.getId())).isEmpty();
    }
}
