package com.gmalykhin.spring.boot.spring_boot_rest_new.controller;

import com.gmalykhin.spring.boot.spring_boot_rest_new.dto.AverageSalaryByDepartmentDTO;
import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.Department;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.IdFieldInPostMethod;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.IdFieldIsZero;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.IncorrectFieldData;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.NoSuchEntityFoundInDBException;
import com.gmalykhin.spring.boot.spring_boot_rest_new.service.MyService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.gmalykhin.spring.boot.spring_boot_rest_new.util.Utils.*;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    private final MyService myService;

    @Autowired
    public DepartmentController(MyService myService) {
        this.myService = myService;
    }

    @GetMapping
    public List<Department> showAllDepartments() {

        var allDepartments = myService.getAllDepartments();

        if (allDepartments.isEmpty()) {
            throw new NoSuchEntityFoundInDBException();
        }
        return allDepartments;
    }

    @GetMapping("/{id}")
    public Department getDepartment(@PathVariable String id) throws NumberFormatException {
        return myService.getDepartment(Integer.parseInt(id.trim()));
    }

    @GetMapping("/average-salary-by-department")
    public List<AverageSalaryByDepartmentDTO> getAvgSalaryByDept() {

        var result = myService.getAverageSalaryByDepartment();

        if (result.isEmpty()) {
            throw new NoSuchEntityFoundInDBException();
        }
        return result;
    }

    @PostMapping
    public ResponseEntity<Department> addNewDepartment(@Valid @RequestBody Department department
                                                                                    , BindingResult bindingResult) {

        if (department.getId() != 0) {
            throw new IdFieldInPostMethod();
        }

        checkEntityFieldsIfNull(department);

        if (bindingResult.hasErrors()) {
            throw new IncorrectFieldData(errorsToString(bindingResult.getFieldErrors()));
        }

        myService.existenceOfTheDepartmentWithSuchNameInDB(department.getDepartmentName());
        checkDepartmentMinMaxSalary(department.getMinSalary(), department.getMaxSalary());
        department.setDepartmentName(department.getDepartmentName().toUpperCase());
        myService.saveDepartment(department);

        return new ResponseEntity<>(department, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateDepartment(@Valid @RequestBody Department department, BindingResult bindingResult) {

        var id = department.getId();

        if (id <= 0) {
            throw new IdFieldIsZero(department);
        }

        if (myService.getDepartment(id) == null) {
            throw new NoSuchEntityFoundInDBException(department.getId());
        }

        var repoDepartment = myService.getDepartment(id);

        checkEntityFieldsIfNullThenFill(department, repoDepartment);

        if (bindingResult.hasErrors()) {
            throw new IncorrectFieldData(errorsToString(bindingResult.getFieldErrors()));
        }
        department.setDepartmentName(department.getDepartmentName().toUpperCase());

        if (!department.getDepartmentName().equals(repoDepartment.getDepartmentName())) {
            myService.existenceOfTheDepartmentWithSuchNameInDB(department.getDepartmentName());
        }
        checkDepartmentMinMaxSalary(department.getMinSalary(), department.getMaxSalary());

        var info = "The department was successfully updated";

        if (!(department.equals(repoDepartment))) {

            if (Double.compare(repoDepartment.getMinSalary(), department.getMinSalary()) != 0
                    || Double.compare(repoDepartment.getMaxSalary(), department.getMaxSalary()) != 0) {

                if (myService.checkEmpsSalaryIfMinOrMaxSalaryWasEdited(department)) {
                    info += ". One or more employees had their salary changed in accordance " +
                            "with the minimum and maximum salaries for this department";
                }
            }
            myService.saveDepartment(department);
        }
        return new ResponseEntity<>(info, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public String deleteDepartment(@PathVariable String id) throws NumberFormatException {

        var intId = Integer.parseInt(id.trim());
        var department = myService.getDepartment(intId);

        if (department == null) {
            throw new NoSuchEntityFoundInDBException(intId);
        }
        myService.deleteDepartment(intId);
        return "Department with ID = " + id + " was successfully deleted";
    }
}
