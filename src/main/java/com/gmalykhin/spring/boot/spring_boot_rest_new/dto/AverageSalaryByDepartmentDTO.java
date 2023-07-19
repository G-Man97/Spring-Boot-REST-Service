package com.gmalykhin.spring.boot.spring_boot_rest_new.dto;

public record AverageSalaryByDepartmentDTO(String departmentName, Double averageSalary) {
    public AverageSalaryByDepartmentDTO(String departmentName, Double averageSalary) {
        this.departmentName = departmentName;
        this.averageSalary = (double) Math.round(averageSalary * 100) / 100;
    }
}