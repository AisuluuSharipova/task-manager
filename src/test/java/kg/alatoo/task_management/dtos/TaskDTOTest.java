package kg.alatoo.task_management.dtos;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class TaskDTOTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenFieldsAreValid_thenNoViolations() {
        TaskDTO dto = new TaskDTO(
                1L,
                "Implement login",
                "Create login and registration endpoints",
                "New",
                "High",
                LocalDate.now(),
                LocalDate.now().plusDays(3),
                1L
        );

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenRequiredFieldsAreBlank_thenViolationsOccur() {
        TaskDTO dto = new TaskDTO();

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(dto);
        assertThat(violations).hasSizeGreaterThanOrEqualTo(5);
    }

    @Test
    void whenEndDateIsInPast_thenViolationOccurs() {
        TaskDTO dto = new TaskDTO(
                1L,
                "Test",
                "Description",
                "New",
                "Low",
                LocalDate.now(),
                LocalDate.now().minusDays(1),
                2L
        );

        Set<ConstraintViolation<TaskDTO>> violations = validator.validate(dto);
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("endDate"));
    }
}
