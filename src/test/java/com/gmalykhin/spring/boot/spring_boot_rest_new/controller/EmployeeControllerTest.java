package com.gmalykhin.spring.boot.spring_boot_rest_new.controller;

import com.gmalykhin.spring.boot.spring_boot_rest_new.dto.EmployeeDTO;
import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.Department;
import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.Employee;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.IdFieldInPostMethod;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.IdFieldIsZero;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.IncorrectFieldData;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.NoSuchEntityFoundInDBException;
import com.gmalykhin.spring.boot.spring_boot_rest_new.service.MyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static java.time.LocalDate.parse;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private MyService myService;

    @InjectMocks
    private EmployeeController employeeController;

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


//------- showAllEmployeesTest() ---------------------------------------------------------------------------------------
    @Test
    @DisplayName("showAllEmployees_List_Is_Not_Empty")
    void showAllEmployees_Should_True_When_List_Of_Employees_Is_Not_Empty() {

        when(myService.getAllEmployees())
                .thenReturn(List.of(employeeFactory(10,"Ivan","Ivanov"
                                                        , parse("1975-05-12"), 2000D, null)));

        assertEquals(List.of(employeeFactory(10,"Ivan","Ivanov"
                                                        , parse("1975-05-12"), 2000D, null))
                , employeeController.showAllEmployees());
    }

    @Test
    @DisplayName("showAllEmployees_List_Is_Empty")
    void showAllEmployees_Should_Throw_Exception_When_List_Of_Employees_Is_Empty() {

        when(myService.getAllEmployees()).thenReturn(new ArrayList<>());

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> employeeController.showAllEmployees());
    }


//------- getEmployeeTest() --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("getEmployee_List_Is_Not_Empty")
    void getEmployee_Should_True_When_List_Is_Not_Empty() {

        when(myService.getEmployee(anyInt()))
                .thenReturn(employeeFactory(7, "Ivan", "Sidorov"
                                                , parse("1974-05-14"), 1500D, null));

        assertEquals(employeeFactory(7, "Ivan", "Sidorov"
                                                    , parse("1974-05-14"), 1500D, null)
                , employeeController.getEmployee("3"));
    }

//    @Test
//    @DisplayName("getEmployee_List_Is_Empty")
//    void getEmployee_Should_Throw_Exception_When_List_Is_Empty() {
//
//        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
//                () -> employeeController.getEmployee("0"));
//    }

    @Test
    @DisplayName("getEmployee_Invalid_Id")
    void getEmployee_Should_Throw_Exception_When_Invalid_Id() {

        assertThrowsExactly(NumberFormatException.class,
                () -> employeeController.getEmployee("invalid_id"));
    }


//------ getEmployeesByDepartmentsTest() -------------------------------------------------------------------------------
    @Test
    @DisplayName("getEmployeesByDepartments_List_Is_Not_Empty")
    void getEmployeesByDepartments_Should_True_When_List_Of_EmployeeDTO_Is_Not_Empty() {

        when(myService.getAllEmployeesByDepartments())
                .thenReturn(List.of(new EmployeeDTO(10,"Ivan","Ivanov", 2000D, "SALES")));

        assertEquals(List.of(new EmployeeDTO(10,"Ivan","Ivanov", 2000D, "SALES"))
                , employeeController.getEmployeesByDepartments());
    }

    @Test
    @DisplayName("getEmployeesByDepartments_List_Is_Empty")
    void getEmployeesByDepartments_Should_Throw_Exception_When_List_Of_EmployeeDTO_Is_Empty() {

        when(myService.getAllEmployeesByDepartments()).thenReturn(new ArrayList<>());

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> employeeController.getEmployeesByDepartments());
    }


//------- searchEmployeeWithTwoParametersTest() ------------------------------------------------------------------------
    @Test
    @DisplayName("searchEmployee_With_Two_Parameters_First_Date_Is_Before_Second_Date")
    void searchEmployee_With_Two_Parameters_Should_True_When_First_Date_Is_Before_Second_Date() {

        when(myService.searchEmployee(isA(LocalDate.class), isA(LocalDate.class)))
                .thenReturn(List.of(new EmployeeDTO(10,"Ivan","Ivanov", 2000D, "SALES")));

        assertEquals(List.of(new EmployeeDTO(10,"Ivan","Ivanov", 2000D, "SALES"))
                , employeeController.searchEmployee(" 1990-01-01 ", " 2000-10-15 "));
    }

    @Test
    @DisplayName("searchEmployee_With_Two_Parameters_Second_Date_Is_Before_First_Date")
    void searchEmployee_With_Two_Parameters_Should_True_When_Second_Date_Is_Before_First_Date() {

        when(myService.searchEmployee(isA(LocalDate.class), isA(LocalDate.class)))
                .thenReturn(List.of(new EmployeeDTO(10,"Ivan","Ivanov", 2000D, "SALES")));

        assertEquals(List.of(new EmployeeDTO(10,"Ivan","Ivanov", 2000D, "SALES"))
                , employeeController.searchEmployee("2001-08-05", "1971-04-12"));
    }

    @Test
    @DisplayName("searchEmployee_With_Two_Parameters_First_Date_Is_Equal_Second_Date")
    void searchEmployee_With_Two_Parameters_Should_True_When_First_Date_Is_Equal_Second_Date() {

        when(myService.searchEmployee(isA(LocalDate.class), isA(LocalDate.class)))
                .thenReturn(List.of(new EmployeeDTO(10,"Ivan","Ivanov", 2000D, "SALES")));

        assertEquals(List.of(new EmployeeDTO(10,"Ivan","Ivanov", 2000D, "SALES"))
                , employeeController.searchEmployee("2000-01-01", "2000-01-01"));
    }

    @Test
    @DisplayName("searchEmployee_With_Two_Parameters_First_Date_Is_Before_Second_Date_List_Is_Empty")
    void searchEmployee_With_Two_Parameters_Should_True_When_First_Date_Is_Before_Second_Date_List_Is_Empty() {

        when(myService.searchEmployee(isA(LocalDate.class), isA(LocalDate.class)))
                .thenReturn(new ArrayList<>());

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> employeeController.searchEmployee("1990-01-01", "2000-10-15"));
    }

    @Test
    @DisplayName("searchEmployee_With_Two_Parameters_Second_Date_Is_Before_First_Date_List_Is_Empty")
    void searchEmployee_With_Two_Parameters_Should_True_When_Second_Date_Is_Before_First_Date_List_Is_Empty() {

        when(myService.searchEmployee(isA(LocalDate.class), isA(LocalDate.class)))
                .thenReturn(new ArrayList<>());

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> employeeController.searchEmployee("2001-08-05", "1971-04-12"));
    }

    @Test
    @DisplayName("searchEmployee_With_Two_Parameters_First_Date_Is_Equal_Second_Date_List_Is_Empty")
    void searchEmployee_With_Two_Parameters_Should_True_When_First_Date_Is_Equal_Second_Date_List_Is_Empty() {

        when(myService.searchEmployee(isA(LocalDate.class), isA(LocalDate.class)))
                .thenReturn(new ArrayList<>());

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> employeeController.searchEmployee("2000-01-01", "2000-01-01"));
    }

    @Test
    @DisplayName("searchEmployee_With_Two_Parameters_First_Date_Is_Invalid")
    void searchEmployee_With_Two_Parameters_Should_True_When_First_Date_Is_Invalid() {

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("1990-15-01", "2000-10-15"));

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("1990-01-41", "2000-10-15"));

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("19900-01-01", "2000-10-15"));

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("invalid_date", "2000-10-15"));
    }

    @Test
    @DisplayName("searchEmployee_With_Two_Parameters_Second_Date_Is_Invalid")
    void searchEmployee_With_Two_Parameters_Should_True_When_Second_Date_Is_Invalid() {

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("2001-08-05", "1971-04-41"));

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("2001-08-05", "1971-15-12"));

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("2001-08-05", "19711-04-12"));

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("2001-08-05", "invalid_date"));
    }


//------- searchEmployeeWithOneParameterTest() -------------------------------------------------------------------------
    @Test
    @DisplayName("searchEmployee_With_One_Parameter_Input_Is_Valid")
    void searchEmployee_With_One_Parameter_Should_True_When_Input_Is_Valid() {

        when(myService.searchEmployee(isA(LocalDate.class), isA(LocalDate.class)))
                .thenReturn(List.of(new EmployeeDTO(10,"Ivan","Ivanov", 2000D, "SALES")));

        assertEquals(List.of(new EmployeeDTO(10,"Ivan","Ivanov", 2000D, "SALES"))
                , employeeController.searchEmployee(" 1990-01-01 "));
    }

    @Test
    @DisplayName("searchEmployee_With_One_Parameter_List_Is_Empty")
    void searchEmployee_With_One_Parameter_Should_True_When_List_Is_Empty() {

        when(myService.searchEmployee(isA(LocalDate.class), isA(LocalDate.class)))
                .thenReturn(new ArrayList<>());

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> employeeController.searchEmployee(" 1990-01-01  "));
    }

    @Test
    @DisplayName("searchEmployee_With_One_Parameter_Input_Is_Invalid")
    void searchEmployee_With_One_Parameter_Should_True_When_Input_Is_Invalid() {

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("1990-08-51"));

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("1990-15-01"));

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("19900-08-51"));

        assertThrowsExactly(DateTimeParseException.class,
                () -> employeeController.searchEmployee("invalid_date"));
    }


//------- addNewEmployeeTest() -----------------------------------------------------------------------------------------
    @Test
    @DisplayName("addNewEmployee_Input_Is_Valid")
    void addNewEmployee_Should_True_When_Input_Is_Valid() {

        var jsonDepartment = departmentFactory(5, null, null, null);
        var jsonEmployee = employeeFactory(0, "Alexey", "Sidorov"
                                           , parse("1995-04-17"), 1500D, jsonDepartment);

        BindingResult bindingResult = new DataBinder(jsonEmployee).getBindingResult();

        var department = departmentFactory(5, "IT_PROG", 1350D, 5500D);
        var employeeWithDepartment = employeeFactory(0, "Alexey", "Sidorov"
                                                         , parse("1995-04-17"), 1500D, department);

        when(myService.checkEmployeesDepartmentFields(any(Employee.class))).thenReturn(employeeWithDepartment);
        doNothing().when(myService).saveEmployee(any(Employee.class));

        assertEquals(new ResponseEntity<>(employeeFactory(0, "Alexey", "Sidorov"
                                                , parse("1995-04-17"), 1500D, department), HttpStatus.CREATED),
                employeeController.addNewEmployee(jsonEmployee, bindingResult));
    }

    @Test
    @DisplayName("addNewEmployee_Id_Is_Not_Zero")
    void addNewEmployee_Should_Throw_Exception_When_Id_Is_Not_Zero() {

        var jsonEmployee = employeeFactory(1, "alexey", "sidorov"
                , parse("1995-04-17"), 1500D, null);

        BindingResult bindingResult = new DataBinder(jsonEmployee).getBindingResult();

        assertThrowsExactly(IdFieldInPostMethod.class,
                () -> employeeController.addNewEmployee(jsonEmployee, bindingResult));
    }

    @Test
    @DisplayName("addNewEmployee_Department_Id_Is_Zero")
    void addNewEmployee_Should_Throw_Exception_When_Department_Id_Is_Zero() {

        var jsonEmployee = employeeFactory(0, "alexey", "sidorov"
                , parse("1995-04-17"), 1500D, null);

        jsonEmployee.setDepartment(new Department());
        BindingResult bindingResult = new DataBinder(jsonEmployee).getBindingResult();

        assertThrowsExactly(IncorrectFieldData.class,
                () -> employeeController.addNewEmployee(jsonEmployee, bindingResult));
    }


//------- updateEmployeeTest() -----------------------------------------------------------------------------------------
    @Test
    @DisplayName("updateEmployee_Edit_Name_Input_Is_Valid")
    void updateEmployee_Should_True_When_Edit_Name_Input_Is_Valid() {

        var department = departmentFactory(5, "IT_PROG", 1350D, 5500D);

        var jsonEmployee = employeeFactory(7, "Ivan", "Sidorov", null, 1500D, department);

        var repoEmployee = employeeFactory(7, "Alexey", "Sidorov"
                , parse("1995-04-17"), 1500D, department);

        BindingResult bindingResult = new DataBinder(jsonEmployee).getBindingResult();


        when(myService.getEmployee(anyInt())).thenReturn(repoEmployee);
        doNothing().when(myService).saveEmployee(any(Employee.class));

        assertEquals(employeeFactory(7, "Ivan", "Sidorov", parse("1995-04-17"), 1500D, department)
                , employeeController.updateEmployee(jsonEmployee, bindingResult));
    }

    @Test
    @DisplayName("updateEmployee_New_Department_With_Big_MinSalary_Input_Is_Valid")
    void updateEmployee_Should_True_When_New_Department_With_Big_MinSalary_Input_Is_Valid() {

        var repoDepartment = departmentFactory(3, "SALES", 850D, 5000D);
        var newDepartment = departmentFactory(5, "MANAGEMENT", 6000D, 10000D);

        var jsonEmployee = employeeFactory(7, "Ivan", "Sidorov", null, null, newDepartment);

        var repoEmployee = employeeFactory(7, "Ivan", "Sidorov"
                , parse("1995-04-17"), 2000D, repoDepartment);

        BindingResult bindingResult = new DataBinder(jsonEmployee).getBindingResult();


        when(myService.getEmployee(anyInt())).thenReturn(repoEmployee);
        when(myService.checkEmployeesDepartmentFields(any(Employee.class))).thenReturn(jsonEmployee);
        doNothing().when(myService).saveEmployee(any(Employee.class));

        assertEquals(employeeFactory(7, "Ivan", "Sidorov", parse("1995-04-17"), 6000D, newDepartment)
                , employeeController.updateEmployee(jsonEmployee, bindingResult));
    }

    @Test
    @DisplayName("updateEmployee_New_Department_With_Little_MaxSalary_Input_Is_Valid")
    void updateEmployee_Should_True_When_New_Department_With_Little_MaxSalary_Input_Is_Valid() {

        var repoDepartment = departmentFactory(5, "MANAGEMENT", 6000D, 10000D);
        var newDepartment = departmentFactory(3, "SALES", 850D, 5000D);

        var jsonEmployee = employeeFactory(7, "Ivan", "Sidorov", null, null, newDepartment);

        var repoEmployee = employeeFactory(7, "Ivan", "Sidorov"
                , parse("1995-04-17"), 6000D, repoDepartment);

        BindingResult bindingResult = new DataBinder(jsonEmployee).getBindingResult();


        when(myService.getEmployee(anyInt())).thenReturn(repoEmployee);
        when(myService.checkEmployeesDepartmentFields(any(Employee.class))).thenReturn(jsonEmployee);
        doNothing().when(myService).saveEmployee(any(Employee.class));

        assertEquals(employeeFactory(7, "Ivan", "Sidorov", parse("1995-04-17"), 5000D, newDepartment)
                , employeeController.updateEmployee(jsonEmployee, bindingResult));
    }

    @Test
    @DisplayName("updateEmployee_Id_Is_Zero")
    void updateEmployee_Should_Throw_Exception_When_Id_Is_Zero() {

        var jsonEmployee = employeeFactory(0, "Ivan", "Sidorov", null, null, null);

        BindingResult bindingResult = new DataBinder(jsonEmployee).getBindingResult();

        assertThrowsExactly(IdFieldIsZero.class,
                () -> employeeController.updateEmployee(jsonEmployee, bindingResult));
    }

    @Test
    @DisplayName("updateEmployee_No_Employee_Found_With_Such_Id")
    void updateEmployee_Should_Throw_Exception_When_No_Employee_Found_With_Such_Id() {

        var jsonEmployee = employeeFactory(3, "Ivan", "Sidorov", null, null, null);

        BindingResult bindingResult = new DataBinder(jsonEmployee).getBindingResult();

        when(myService.getEmployee(anyInt())).thenReturn(null);

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> employeeController.updateEmployee(jsonEmployee, bindingResult));
    }

    @Test
    @DisplayName("updateEmployee_Edit_Birthday")
    void updateEmployee_Should_Throw_Exception_When_Edit_Birthday() {

        var jsonEmployee = employeeFactory(3, "Ivan", "Sidorov", parse("1980-03-17") , null, null);
        var repoEmployee = employeeFactory(3, "Ivan", "Sidorov", parse("1975-05-15") , 1700D, null);

        BindingResult bindingResult = new DataBinder(jsonEmployee).getBindingResult();

        when(myService.getEmployee(anyInt())).thenReturn(repoEmployee);

        assertThrowsExactly(IncorrectFieldData.class,
                () -> employeeController.updateEmployee(jsonEmployee, bindingResult));
    }

    @Test
    @DisplayName("updateEmployee_Department_Is_Null_And_Edit_Salary")
    void updateEmployee_Should_Throw_Exception_When_Department_Is_Null_And_Edit_Salary() {

        var jsonEmployee = employeeFactory(3, "Ivan", "Sidorov", null , 2500D, null);
        var repoEmployee = employeeFactory(3, "Ivan", "Sidorov", parse("1975-05-15") , 1700D, null);

        BindingResult bindingResult = new DataBinder(jsonEmployee).getBindingResult();

        when(myService.getEmployee(anyInt())).thenReturn(repoEmployee);

        assertThrowsExactly(IncorrectFieldData.class,
                () -> employeeController.updateEmployee(jsonEmployee, bindingResult));
    }
}

