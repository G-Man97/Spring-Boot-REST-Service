package com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling;

public class NoSuchEntityFoundInDBException extends RuntimeException {

    public NoSuchEntityFoundInDBException(String message) {
        super(message);
    }

    public NoSuchEntityFoundInDBException() {
        super("No such entity(s) found in DB");
    }

    public NoSuchEntityFoundInDBException(int id) {
        super("There is no such raw with ID = " + id + " in DB");
    }
}
