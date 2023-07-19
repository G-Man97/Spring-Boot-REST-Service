package com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling;

public class IncorrectFieldData extends RuntimeException{

    public IncorrectFieldData(String message) {
        super(message);
    }
}
