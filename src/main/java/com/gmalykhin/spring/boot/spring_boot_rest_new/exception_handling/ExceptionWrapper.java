package com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling;

public class ExceptionWrapper {

    private String info;

    public ExceptionWrapper(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
