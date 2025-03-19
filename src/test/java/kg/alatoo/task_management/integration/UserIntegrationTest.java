package kg.alatoo.task_management.integration;

import kg.alatoo.task_management.dtos.UserDTO;
import kg.alatoo.task_management.entities.User;
import kg.alatoo.task_management.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void testCreateUser() {
        UserDTO userDTO = new UserDTO(null, "John", "Doe", "john.doe@example.com");

        ResponseEntity<UserDTO> response = restTemplate.postForEntity(
                "/api/users", userDTO, UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("John");

        User savedUser = userRepository.findByEmail("john.doe@example.com").orElseThrow();
        assertThat(savedUser.getFirstName()).isEqualTo("John");
    }

    @Test
    void testGetUserById() {
        User user = userRepository.save(new User(null, "Alice", "Smith", "alice.smith@example.com"));

        ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                "/api/users/" + user.getId(), UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirstName()).isEqualTo("Alice");
    }

    @Test
    void testGetUserByEmail() {
        User user = userRepository.save(new User(null, "Bob", "Brown", "bob.brown@example.com"));

        ResponseEntity<UserDTO> response = restTemplate.getForEntity(
                "/api/users/search/" + user.getEmail(), UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("bob.brown@example.com");
    }

    @Test
    void testGetUsersByName() {
        userRepository.save(new User(null, "Charlie", "White", "charlie.white@example.com"));
        userRepository.save(new User(null, "Charlie", "Black", "charlie.black@example.com"));

        ResponseEntity<UserDTO[]> response = restTemplate.getForEntity(
                "/api/users/search?firstName=Charlie&lastName=White", UserDTO[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody()[0].getLastName()).isEqualTo("White");
    }

    @Test
    void testUpdateUser() {
        User user = userRepository.save(new User(null, "David", "Miller", "david.miller@example.com"));

        UserDTO updatedUser = new UserDTO(user.getId(), "David", "Johnson", "david.johnson@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(updatedUser, headers);

        ResponseEntity<UserDTO> response = restTemplate.exchange(
                "/api/users/" + user.getId(), HttpMethod.PUT, requestEntity, UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getLastName()).isEqualTo("Johnson");

        User updatedInDb = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updatedInDb.getLastName()).isEqualTo("Johnson");
    }

    @Test
    void testDeleteUser() {
        User user = userRepository.save(new User(null, "Frank", "Taylor", "frank.taylor@example.com"));

        restTemplate.delete("/api/users/" + user.getId());

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}
