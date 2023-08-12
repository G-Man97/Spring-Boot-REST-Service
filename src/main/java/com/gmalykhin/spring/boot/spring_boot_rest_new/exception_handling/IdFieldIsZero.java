package com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling;

import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.BaseEntity;

public class IdFieldIsZero extends IncorrectFieldData {

    public <T extends BaseEntity> IdFieldIsZero (T entity) {
        this(entity.getClass().getSimpleName().toLowerCase());
    }

    private IdFieldIsZero(String entityName) {
        super("To edit the " + entityName + " you need write your " + entityName + " id. The id can not be 0");
    }
}
