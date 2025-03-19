package kg.alatoo.task_management.controllers;

import kg.alatoo.task_management.exceptions.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionHandlerControllerTest {

    private ExceptionHandlerController exceptionHandlerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandlerController = new ExceptionHandlerController();
    }

    @Test
    void handleNotFoundException() {
        NotFoundException ex = new NotFoundException("Resource not found");

        ResponseEntity<Map<String, Object>> response = exceptionHandlerController.handleNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Resource not found", response.getBody().get("message"));
    }

    @Test
    void handleBindingError() throws NoSuchMethodException {
        MethodParameter methodParameter = new MethodParameter(
                this.getClass().getDeclaredMethod("handleBindingError"), -1
        );

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "userDTO");
        bindingResult.addError(new FieldError("userDTO", "email", "Invalid email format"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<Map<String, Object>> response = exceptionHandlerController.handleBindingError(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().containsKey("errors"));
        assertEquals("Invalid email format", ((Map<?, ?>) response.getBody().get("errors")).get("email"));
    }

    @Test
    void handleDatabaseConstraintException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Unique constraint violated");

        ResponseEntity<Map<String, Object>> response = exceptionHandlerController.handleDatabaseConstraintException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().get("message").toString().contains("Database constraint violation"));
    }

    @Test
    void handleException() {
        Exception ex = new RuntimeException("Unexpected error occurred");

        ResponseEntity<Map<String, Object>> response = exceptionHandlerController.handleException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().get("message").toString().contains("An unexpected error occurred"));
    }
}
