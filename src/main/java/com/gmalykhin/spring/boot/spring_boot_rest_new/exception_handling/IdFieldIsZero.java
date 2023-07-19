package com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling;

import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.EntityMarker;

public class IdFieldIsZero extends IncorrectFieldData {

    public <T extends EntityMarker> IdFieldIsZero (T entity) {
        this(entity.getClass().getSimpleName().toLowerCase());
    }
    private IdFieldIsZero(String className) {
        super("To edit the " + className + " you need write your " + className + " id. The id can not be 0");
    }
}
