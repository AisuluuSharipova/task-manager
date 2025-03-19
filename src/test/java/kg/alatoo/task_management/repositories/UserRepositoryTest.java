package kg.alatoo.task_management.repositories;

import kg.alatoo.task_management.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = new User(null, "Aisuluu", "Zhoodarbek", "aisuluu.zhoodarbek@example.com");
        user2 = new User(null, "Aisuluu", "Sharipova", "aisuluu.sharipova@example.com");

        userRepository.saveAll(List.of(user1, user2));
    }

    @Test
    void findUserByEmail() {
        Optional<User> foundUser = userRepository.findByEmail("aisuluu.zhoodarbek@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getFirstName()).isEqualTo("Aisuluu");
        assertThat(foundUser.get().getLastName()).isEqualTo("Zhoodarbek");
    }

    @Test
    void returnEmptyIfEmailNotFound() {
        Optional<User> foundUser = userRepository.findByEmail("unknown@example.com");

        assertThat(foundUser).isEmpty();
    }

    @Test
    void findUsersByFirstNameAndLastName() {
        List<User> foundUsers = userRepository.findByFirstNameAndLastName("Aisuluu", "Zhoodarbek");

        assertThat(foundUsers).hasSize(1);
        assertThat(foundUsers.get(0).getEmail()).isEqualTo("aisuluu.zhoodarbek@example.com");
    }

    @Test
    void returnEmptyListIfNameNotFound() {
        List<User> foundUsers = userRepository.findByFirstNameAndLastName("John", "Doe");

        assertThat(foundUsers).isEmpty();
    }
}
