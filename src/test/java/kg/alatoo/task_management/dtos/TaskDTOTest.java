package kg.alatoo.task_management.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidTaskDTO() {
        TaskDTO taskDTO = new TaskDTO(
                1L,
                "Valid Title",
                "Valid Description",
                "IN_PROGRESS",
                "HIGH",
                new Date(),
                LocalDate.now().plusDays(1),
                10L
        );

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
        assertTrue(violations.isEmpty(), "Valid TaskDTO should not have any validation errors");
    }

    @Test
    void testInvalidTaskDTO_TitleBlank() {
        TaskDTO taskDTO = new TaskDTO(
                1L,
                "",
                "Valid Description",
                "IN_PROGRESS",
                "HIGH",
                new Date(),
                LocalDate.now().plusDays(1),
                10L
        );

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Title is required")));
    }

    @Test
    void testInvalidTaskDTO_DescriptionBlank() {
        TaskDTO taskDTO = new TaskDTO(
                1L,
                "Valid Title",
                "",
                "IN_PROGRESS",
                "HIGH",
                new Date(),
                LocalDate.now().plusDays(1),
                10L
        );

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Description is required")));
    }

    @Test
    void testInvalidTaskDTO_EndDatePast() {
        TaskDTO taskDTO = new TaskDTO(
                1L,
                "Valid Title",
                "Valid Description",
                "IN_PROGRESS",
                "HIGH",
                new Date(),
                LocalDate.now().minusDays(1),
                10L
        );

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("End date must be in the future")));
    }

    @Test
    void testInvalidTaskDTO_AssignedUserNull() {
        TaskDTO taskDTO = new TaskDTO(
                1L,
                "Valid Title",
                "Valid Description",
                "IN_PROGRESS",
                "HIGH",
                new Date(),
                LocalDate.now().plusDays(1),
                null
        );

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(taskDTO);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Assigned user is required")));
    }
}
