package com.gmalykhin.spring.boot.spring_boot_rest_new.service;

import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.Department;
import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.Employee;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.NoSuchEntityFoundInDBException;
import com.gmalykhin.spring.boot.spring_boot_rest_new.repository.DepartmentRepository;
import com.gmalykhin.spring.boot.spring_boot_rest_new.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static java.time.LocalDate.parse;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private MyServiceImpl myServiceImpl;

    private Employee employeeFactory(int id, String name, String surname, LocalDate birthday, Double salary, Department department) {

        var employee = new Employee();

        employee.setId(id);
        employee.setName(name);
        employee.setSurname(surname);
        employee.setBirthday(birthday);
        employee.setSalary(salary);
        employee.setDepartment(department);

        return employee;
    }

    private Department departmentFactory(int id, String departmentName, Double minSalary, Double maxSalary) {

        var department = new Department();

        department.setId(id);
        department.setDepartmentName(departmentName);
        department.setMinSalary(minSalary);
        department.setMaxSalary(maxSalary);

        return  department;
    }

//------- checkEmployeesDepartmentFieldsTest() -------------------------------------------------------------------------
//    @Test
//    @DisplayName("checkEmployeesDepartmentFields_Input_Is_Valid")
//    void checkEmployeesDepartmentFields_Should_True_When_Input_Is_Valid() {
//
//        var jsonDepartment = departmentFactory(5, null, null, null);
//        var jsonEmployee = employeeFactory(10, "Ivan", "Ivanov", parse("1995-07-13")
//                                                                                , 1750D, jsonDepartment);
//        var repoDepartment = departmentFactory(5, "SALES", 1500D, 6000D);
//
//        when(myServiceImpl.getDepartment(anyInt())).thenReturn(repoDepartment);
//
//        assertEquals(employeeFactory(10, "Ivan", "Ivanov", parse("1995-07-13")
//                                                                            , 1750D, repoDepartment)
//                , myServiceImpl.checkEmployeesDepartmentFields(jsonEmployee));
//    }

//------- checkEmpsSalaryIfMinOrMaxSalaryWasEditedTest() ---------------------------------------------------------------
    @Test
    @DisplayName("checkEmpsSalaryIfMinOrMaxSalaryWasEdited_Employees_Salary_Was_Edit")
    void checkEmpsSalaryIfMinOrMaxSalaryWasEdited_Should_True_When_Employees_Salary_Was_Edit() {

        var department = departmentFactory(5, "SALES", 2000D, 6000D);
        var employee1 = employeeFactory(11, "Ivan", "Ivanov", parse("1995-07-13")
                                                                                , 1750D, department);
        var employee2 = employeeFactory(17, "Anton", "Petrov", parse("1993-12-07")
                                                                                , 1600D, department);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        when(myServiceImpl.employeesInDepartment(anyInt())).thenReturn(employees);

        assertTrue(myServiceImpl.checkEmpsSalaryIfMinOrMaxSalaryWasEdited(department));
    }

    @Test
    @DisplayName("checkEmpsSalaryIfMinOrMaxSalaryWasEdited_Employees_Salary_Was_Not_Edit")
    void checkEmpsSalaryIfMinOrMaxSalaryWasEdited_Should_False_When_Employees_Salary_Was_Not_Edit() {

        var department = departmentFactory(5, "SALES", 2000D, 6000D);
        var employee1 = employeeFactory(11, "Ivan", "Ivanov", parse("1995-07-13")
                                                                                , 2415D, department);
        var employee2 = employeeFactory(17, "Anton", "Petrov", parse("1993-12-07")
                                                                                , 2150D, department);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        when(myServiceImpl.employeesInDepartment(anyInt())).thenReturn(employees);

        assertFalse(myServiceImpl.checkEmpsSalaryIfMinOrMaxSalaryWasEdited(department));
    }

    @Test
    @DisplayName("getEmployee_Employee_Is_Found")
    void getEmployee_Should_True_When_Employee_Is_Found() {

        var employee = employeeFactory(11, "Ivan", "Ivanov", parse("1995-07-13")
                , 2415D, null);

        when(employeeRepository.findById(11)).thenReturn(Optional.of(employee));

        assertEquals(myServiceImpl.getEmployee(11), employee);
    }

    @Test
    @DisplayName("getEmployee_Employee_Is_Not_Found")
    void getEmployee_Should_True_When_Employee_Is_Not_Found() {

        when(employeeRepository.findById(11)).thenReturn(Optional.empty());

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> myServiceImpl.getEmployee(11));
    }

    @Test
    @DisplayName("getDepartment_Department_Is_Found")
    void getDepartment_Should_True_When_Department_Is_Found() {

        var department = departmentFactory(5, "SALES", 2000D, 6000D);

        when(departmentRepository.findById(11)).thenReturn(Optional.of(department));

        assertEquals(myServiceImpl.getDepartment(11), department);
    }

    @Test
    @DisplayName("getDepartment_Department_Is_Not_Found")
    void getDepartment_Should_True_When_Department_Is_Not_Found() {

        when(departmentRepository.findById(11)).thenReturn(Optional.empty());

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> myServiceImpl.getDepartment(11));
    }
}