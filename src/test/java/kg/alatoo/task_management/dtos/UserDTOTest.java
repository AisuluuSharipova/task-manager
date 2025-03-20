package kg.alatoo.task_management.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUserDTO() {
        UserDTO userDTO = new UserDTO(
                1L,
                "Aisuluu",
                "Sharipova",
                "sharipova@gmail.com"
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertTrue(violations.isEmpty(), "Valid UserDTO should not have any validation errors");
    }

    @Test
    void testInvalidUserDTO_FirstNameBlank() {
        UserDTO userDTO = new UserDTO(
                1L,
                "",
                "Sharipova",
                "sharipova@gail.com"
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("First name cannot be empty")));
    }

    @Test
    void testInvalidUserDTO_LastNameBlank() {
        UserDTO userDTO = new UserDTO(
                1L,
                "Sharipova",
                "",
                "sharipova@gmail.com"
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Last name cannot be empty")));
    }

    @Test
    void testInvalidUserDTO_EmailNull() {
        UserDTO userDTO = new UserDTO(
                1L,
                "Aisuluu",
                "Sharipova",
                null
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Email is required")));
    }

    @Test
    void testInvalidUserDTO_EmailInvalidFormat() {
        UserDTO userDTO = new UserDTO(
                1L,
                "Aisuluu",
                "Sharipova",
                "sharipova-gmail.com"
        );

        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid email format")));
    }
}
