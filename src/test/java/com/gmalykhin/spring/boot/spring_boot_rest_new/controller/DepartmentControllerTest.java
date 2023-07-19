package com.gmalykhin.spring.boot.spring_boot_rest_new.controller;

import com.gmalykhin.spring.boot.spring_boot_rest_new.dto.AverageSalaryByDepartmentDTO;
import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.Department;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.IdFieldInPostMethod;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.IdFieldIsZero;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.NoSuchEntityFoundInDBException;
import com.gmalykhin.spring.boot.spring_boot_rest_new.service.MyService;
import org.junit.jupiter.api.Assertions;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class DepartmentControllerTest {

    @Mock
    private MyService myService;

    @InjectMocks
    private DepartmentController departmentController;

    private Department departmentFactory(int id, String departmentName, Double minSalary, Double maxSalary) {

        var department = new Department();

        department.setId(id);
        department.setDepartmentName(departmentName);
        department.setMinSalary(minSalary);
        department.setMaxSalary(maxSalary);

        return department;
    }


    //------- showAllDepartmentsTest() -------------------------------------------------------------------------------------
    @Test
    @DisplayName("showAllDepartments_List_Is_Not_Empty")
    void showAllEmployees_Should_True_When_List_Of_Departments_Is_Not_Empty() {

        when(myService.getAllDepartments())
                .thenReturn(List.of(departmentFactory(1, "SALES", 850D, 5000D)));

        assertEquals(List.of(departmentFactory(1, "SALES", 850D, 5000D))
                , departmentController.showAllDepartments());
    }

    @Test
    @DisplayName("showAllDepartments_List_Is_Empty")
    void showAllEmployees_Should_Throw_Exception_When_List_Of_Departments_Is_Empty() {

        when(myService.getAllDepartments()).thenReturn(new ArrayList<>());

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> departmentController.showAllDepartments());
    }


//------- getDepartmentTest() --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("getDepartment_List_Is_Not_Empty")
    void getDepartment_Should_True_When_List_Is_Not_Empty() {

        when(myService.getDepartment(anyInt()))
                .thenReturn(departmentFactory(1, "IT_PROG", 850D, 5000D));

        assertEquals(departmentFactory(1, "IT_PROG", 850D, 5000D)
                , departmentController.getDepartment("3"));
    }

//    @Test
//    @DisplayName("getDepartment_List_Is_Empty")
//    void getDepartment_Should_Throw_Exception_When_List_Is_Empty() {
//
//        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
//                () -> departmentController.getDepartment("0"));
//    }

    @Test
    @DisplayName("getDepartment_Invalid_Id")
    void getDepartment_Should_Throw_Exception_When_Invalid_Id() {

        assertThrowsExactly(NumberFormatException.class,
                () -> departmentController.getDepartment("invalid_id"));
    }


//------ getAvgSalaryByDeptTest() --------------------------------------------------------------------------------------
    @Test
    @DisplayName("getAvgSalaryByDept_List_Is_Not_Empty")
    void getAvgSalaryByDept_Should_True_When_List_Of_Result_Is_Not_Empty() {

        when(myService.getAverageSalaryByDepartment())
                .thenReturn(List.of(new AverageSalaryByDepartmentDTO("SALES", 3564D)));

        assertEquals(List.of(new AverageSalaryByDepartmentDTO("SALES", 3564D))
                , departmentController.getAvgSalaryByDept());
    }

    @Test
    @DisplayName("getAvgSalaryByDept_List_Is_Empty")
    void getAvgSalaryByDept_Should_Throw_Exception_When_List_Of_Result_Is_Empty() {

        when(myService.getAverageSalaryByDepartment()).thenReturn(new ArrayList<>());

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> departmentController.getAvgSalaryByDept());
    }


//------ addNewDepartment() --------------------------------------------------------------------------------------------
    @Test
    @DisplayName("addNewDepartment_Input_Is_Valid")
    void addNewDepartment_Should_True_When_Input_Is_Valid() {

        var jsonDepartment = departmentFactory(0, "SALES", 1350D, 5500D);

        BindingResult bindingResult = new DataBinder(jsonDepartment).getBindingResult();

        doNothing().when(myService).existenceOfTheDepartmentWithSuchNameInDB("SALES");
        doNothing().when(myService).saveDepartment(any(Department.class));

        assertEquals(new ResponseEntity<>(departmentFactory(0, "SALES", 1350D, 5500D)
                                                                                                   , HttpStatus.CREATED)
                , departmentController.addNewDepartment(jsonDepartment, bindingResult));
    }

    @Test
    @DisplayName("addNewDepartment_Id_Is_Not_Zero")
    void addNewDepartment_Should_Throw_Exception_When_Id_Is_Not_Zero() {

        var jsonDepartment = departmentFactory(1, "SALES", 1350D, 5500D);

        BindingResult bindingResult = new DataBinder(jsonDepartment).getBindingResult();

        Assertions.assertThrowsExactly(IdFieldInPostMethod.class,
                () -> departmentController.addNewDepartment(jsonDepartment, bindingResult));
    }


//------- updateDepartmentTest() ---------------------------------------------------------------------------------------
    @Test
    @DisplayName("updateDepartment_Edit_MinSalary_Input_Is_Valid")
    void updateDepartment_Should_True_When_Edit_MinSalary_Input_Is_Valid() {

        var repoDepartment = departmentFactory(1, "SALES", 1350D, 5500D);
        var jsonDepartment = departmentFactory(1, "SALES", 1600D, 5500D);
        var info = "The department was successfully updated. One or more employees had their salary changed " +
                "in accordance with the minimum and maximum salaries for this department";

        BindingResult bindingResult = new DataBinder(jsonDepartment).getBindingResult();

        when(myService.getDepartment(anyInt())).thenReturn(repoDepartment);
        when(myService.checkEmpsSalaryIfMinOrMaxSalaryWasEdited(any(Department.class))).thenReturn(true);
        doNothing().when(myService).saveDepartment(any(Department.class));

        assertEquals(info, departmentController.updateDepartment(jsonDepartment, bindingResult));
    }

    @Test
    @DisplayName("updateDepartment_Edit_MaxSalary_Input_Is_Valid")
    void updateDepartment_Should_True_When_Edit_MaxSalary_Input_Is_Valid() {

        var repoDepartment = departmentFactory(1, "SALES", 1600D, 5500D);
        var jsonDepartment = departmentFactory(1, "SALES", 1600D, 6000D);
        var info = "The department was successfully updated";

        BindingResult bindingResult = new DataBinder(jsonDepartment).getBindingResult();

        when(myService.getDepartment(anyInt())).thenReturn(repoDepartment);
        when(myService.checkEmpsSalaryIfMinOrMaxSalaryWasEdited(any(Department.class))).thenReturn(false);
        doNothing().when(myService).saveDepartment(any(Department.class));

        assertEquals(info, departmentController.updateDepartment(jsonDepartment, bindingResult));
    }

    @Test
    @DisplayName("updateDepartment_Id_Is_Zero")
    void updateDepartment_Should_Throw_Exception_When_Id_Is_Zero() {

        var jsonDepartment = departmentFactory(0, "SALES", 1600D, 6000D);

        BindingResult bindingResult = new DataBinder(jsonDepartment).getBindingResult();

        assertThrowsExactly(IdFieldIsZero.class,
                () -> departmentController.updateDepartment(jsonDepartment, bindingResult));
    }

    @Test
    @DisplayName("updateDepartment_Department_Is_Not_Existence")
    void updateDepartment_Should_Throw_Exception_When_Department_Is_Not_Existence() {

        var jsonDepartment = departmentFactory(3, "SALES", 1600D, 6000D);

        BindingResult bindingResult = new DataBinder(jsonDepartment).getBindingResult();

        when(myService.getDepartment(anyInt())).thenReturn(null);

        assertThrowsExactly(NoSuchEntityFoundInDBException.class,
                () -> departmentController.updateDepartment(jsonDepartment, bindingResult));
    }
}