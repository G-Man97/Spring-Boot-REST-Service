package com.gmalykhin.spring.boot.spring_boot_rest_new.service;

import com.gmalykhin.spring.boot.spring_boot_rest_new.dto.AverageSalaryByDepartmentDTO;
import com.gmalykhin.spring.boot.spring_boot_rest_new.dto.EmployeeDTO;
import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface MyService {

    List<Employee> getAllEmployees();

    void saveEmployee(Employee employee);

    List<Department> getAllDepartments();

    Employee getEmployee(int id);

    void deleteEmployee(int id);

    Department getDepartment(int id);

    void deleteDepartment(int id);

    void saveDepartment(Department department);

    List<AverageSalaryByDepartmentDTO> getAverageSalaryByDepartment();

    List<EmployeeDTO> getAllEmployeesByDepartments();

    List<EmployeeDTO> searchEmployee(LocalDate fDate, LocalDate sDate);

    Employee checkEmployeesDepartmentFields (Employee employee);

    List<Employee> employeesInDepartment (int departmentId);

    boolean checkEmpsSalaryIfMinOrMaxSalaryWasEdited(Department department);

    void existenceOfTheDepartmentWithSuchNameInDB(String departmentName);
}
