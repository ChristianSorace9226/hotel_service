package it.nesea.albergo.hotel_service.exception;

import it.nesea.albergo.hotel_service.dto.response.CustomResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.management.InstanceAlreadyExistsException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> errors.add(e.getDefaultMessage()));
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CustomResponse.error(errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(getError(ex));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomResponse> handleNotFoundException(NotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getError(ex));
    }

    @ExceptionHandler(InstanceAlreadyExistsException.class)
    public ResponseEntity<CustomResponse> handleInstanceAlreadyExistsException(InstanceAlreadyExistsException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(getError(ex));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<CustomResponse> handleBadCredentialsException(BadCredentialsException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(getError(ex));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse> handleGeneralException(Exception ex) {
        log.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(getError(ex));
    }

    private CustomResponse getError(Exception ex) {
        List<String> errors = new ArrayList<>(1);
        errors.add(ex.getMessage());
        return CustomResponse.error(errors);
    }

}
