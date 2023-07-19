package com.gmalykhin.spring.boot.spring_boot_rest_new.repository;

import com.gmalykhin.spring.boot.spring_boot_rest_new.dto.AverageSalaryByDepartmentDTO;
import com.gmalykhin.spring.boot.spring_boot_rest_new.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    @Query("select new com.gmalykhin.spring.boot.spring_boot_rest_new.dto" +
            ".AverageSalaryByDepartmentDTO(d.departmentName, avg(e.salary)) " +
            " from Employee e join Department d ON d.id = e.department.id group by d.departmentName")
    List<AverageSalaryByDepartmentDTO> getAverageSalaryByDepartment();

    Department getDepartmentByDepartmentName(String departmentName);
}
