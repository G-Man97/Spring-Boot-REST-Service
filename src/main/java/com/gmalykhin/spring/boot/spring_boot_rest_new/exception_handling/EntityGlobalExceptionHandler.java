package com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.format.DateTimeParseException;

@ControllerAdvice
public class EntityGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionWrapper> handleException(
            NoSuchEntityFoundInDBException exception){

        return new ResponseEntity<>(new ExceptionWrapper(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionWrapper> handleException(
            Exception exception){

        return new ResponseEntity<>(new ExceptionWrapper(exception.getMessage()
                + ". Please check your input is correct!"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionWrapper> handleException(
            NumberFormatException exception){

        return new ResponseEntity<>(new ExceptionWrapper("Invalid input. An integer was expected")
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionWrapper> handleException(
            DateTimeParseException exception){

        return new ResponseEntity<>(
                new ExceptionWrapper("Use pattern api/employees/search-for-employees-born-in/" +
                "1970-01-12/2001-11-07 or api/employees/search-for-employees-born-in/1970-01-12 " +
                        "or check the existence of the date!"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionWrapper> handleException(
            IncorrectFieldData exception){

        return new ResponseEntity<>(new ExceptionWrapper(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}