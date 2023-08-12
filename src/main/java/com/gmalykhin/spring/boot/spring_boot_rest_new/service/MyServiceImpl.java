package com.gmalykhin.spring.boot.spring_boot_rest_new.service;

import com.gmalykhin.spring.boot.spring_boot_rest_new.dto.AverageSalaryByDepartmentDTO;
import com.gmalykhin.spring.boot.spring_boot_rest_new.dto.EmployeeDTO;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.NoSuchEntityFoundInDBException;
import com.gmalykhin.spring.boot.spring_boot_rest_new.repository.DepartmentRepository;
import com.gmalykhin.spring.boot.spring_boot_rest_new.repository.EmployeeRepository;
import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.*;
import com.gmalykhin.spring.boot.spring_boot_rest_new.exception_handling.IncorrectFieldData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MyServiceImpl implements MyService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public MyServiceImpl(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public Employee getEmployee(int id) {
        return employeeRepository.findById(id).orElseThrow( () -> new NoSuchEntityFoundInDBException(id));
    }

    @Override
    public void deleteEmployee(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Department getDepartment(int id) {
        return departmentRepository.findById(id).orElseThrow( () -> new NoSuchEntityFoundInDBException(id));
    }

    @Override
    public void deleteDepartment(int id) {
        departmentRepository.deleteById(id);
    }

    @Override
    public void saveDepartment(Department department) {
        departmentRepository.save(department);
    }

    @Override
    public List<EmployeeDTO> searchEmployee(LocalDate fDate, LocalDate sDate) {
        return employeeRepository.searchEmployee(fDate, sDate);
    }

    @Override
    public List<AverageSalaryByDepartmentDTO> getAverageSalaryByDepartment() {
        return departmentRepository.getAverageSalaryByDepartment();
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesByDepartments() {
        return employeeRepository.findEmployeesByDepartment();
    }

    @Override
    public List<Employee> employeesInDepartment (int departmentId) {
        return employeeRepository.findEmployeesByDepartmentId(departmentId);
    }

    @Override
    public void existenceOfTheDepartmentWithSuchNameInDB(String departmentName) {
        if (departmentRepository.getDepartmentByDepartmentName(departmentName) != null) {
            throw new IncorrectFieldData("The value of the departmentName field must be unique");
        }
    }

    /* Проверка - существует ли департамент с таким id, а так же какие поля указаны
     * Для удобства ввода и во избежание ошибок ввода осталено только поле id у департамента,
     * остальные поля заполняются автоматически далее по коду.
     * Поэтому если указаны лишние поля, то выбрасывается исключение */
    @Override
    public Employee checkEmployeesDepartmentFields (Employee employee) {
        var departmentId = employee.getDepartment().getId();

        this.getDepartment(departmentId);
        var jsonDepartment = employee.getDepartment();

        if (jsonDepartment.getDepartmentName() != null
                || jsonDepartment.getMinSalary() != null
                || jsonDepartment.getMaxSalary() != null) {
            throw new IncorrectFieldData("Write only the id field for the department");
        } else {
            employee.setDepartment(getDepartment(departmentId));
        }
        return employee;
    }

    /* Проверка если поменялось значение minSalary или maxSalary в department, то берется список
     * employee в этом department и у каждого проверяется попадание salary в диапазон minSalary - maxSalary.
     * Если salary меньше, чем minSalary, тогда salary присваивается значение minSalary.
     * Если salary больше, чем maxSalary, тогда salary присваивается значение maxSalary. */
    @Override
    public boolean checkEmpsSalaryIfMinOrMaxSalaryWasEdited(Department department) {

        var employeesInDept = this.employeesInDepartment(department.getId());
        var minSalaryFlag = false;
        var maxSalaryFlag = false;

        if (!employeesInDept.isEmpty()) {
            for (var e : employeesInDept) {
                if (e.getSalary() < department.getMinSalary()) {
                    e.setSalary(department.getMinSalary());
                    this.saveEmployee(e);
                    minSalaryFlag = true;
                } else if (e.getSalary() > department.getMaxSalary()) {
                    e.setSalary(department.getMaxSalary());
                    this.saveEmployee(e);
                    maxSalaryFlag = true;
                }
            }
        }
        return minSalaryFlag || maxSalaryFlag;
    }
}
