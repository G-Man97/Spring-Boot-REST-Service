package com.gmalykhin.spring.boot.spring_boot_rest_new.repository;

import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.gmalykhin.spring.boot.spring_boot_rest_new.dto.EmployeeDTO;

import java.time.LocalDate;
import java.util.List;


public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findEmployeesByDepartmentId(int id);

    @Query("select new com.gmalykhin.spring.boot.spring_boot_rest_new.dto.EmployeeDTO(e.id, e.name, " +
            " e.surname, e.salary, e.department.departmentName) " +
            "from Employee e where e.birthday between ?1 and ?2 order by e.department.departmentName")
    List<EmployeeDTO> searchEmployee(LocalDate fDate, LocalDate sDate);

    @Query("select new com.gmalykhin.spring.boot.spring_boot_rest_new.dto.EmployeeDTO(e.id, e.name, " +
            " e.surname, e.salary, e.department.departmentName) " +
            "from Employee e join Department d on d.id = e.department.id order by e.department.departmentName")
    List<EmployeeDTO> findEmployeesByDepartment();


}
