package com.logan.bloginfo.exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ErrorHandlerTest {

    @Test
    public void testHandleValidationException() {
        BindingResult bindingResult = mock(BindingResult.class);

        MethodArgumentNotValidException ex = spy(new MethodArgumentNotValidException(null, bindingResult));
        when(ex.getBindingResult().getAllErrors()).thenReturn(List.of(
                new FieldError("myObject", "field1", "error message 1"),
                new FieldError("myObject", "field2", "error message 2")
        ));

        ErrorHandler errorHandler = new ErrorHandler();
        ErrorMessage result = errorHandler.handleValidationException(ex);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getStatusCode());
    }

    @Test
    public void testHandleEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Entity not found");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorMessage result = errorHandler.handleEntityNotFoundException(ex);
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND.value(), result.getStatusCode());
        assertEquals("Entity not found", result.getMessage());
    }

    @Test
    public void testHandleException() {
        Exception ex = new Exception("Internal server error");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorMessage result = errorHandler.handleException(ex);
        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), result.getStatusCode());
    }
}

