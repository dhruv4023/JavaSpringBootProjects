package com.authserver.authserver.base.helper;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.authserver.authserver.user.exceptions.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseObject> handleUserNotFound(UserNotFoundException ex) {
        ResponseObject response = new ResponseObject();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseObject> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        ResponseObject response = new ResponseObject();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ResponseObject> handleInvalidPassword(InvalidPasswordException ex) {
        ResponseObject response = new ResponseObject();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ResponseObject> handleValidationException(ValidationException ex) {
        ResponseObject response = new ResponseObject();
        response.setSuccess(false);
        response.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject> handleGenericException(Exception ex) {
        ResponseObject response = new ResponseObject();
        response.setSuccess(false);
        response.setMessage("Internal server error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
