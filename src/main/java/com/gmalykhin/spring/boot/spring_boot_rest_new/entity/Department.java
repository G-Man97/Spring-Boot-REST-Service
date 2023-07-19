package com.gmalykhin.spring.boot.spring_boot_rest_new.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "departments")
public class Department extends EntityMarker {

    private int id;
    private String departmentName;
    private Double minSalary;
    private Double maxSalary;
    private List<Employee> employee;

    public Department() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "name" , unique = true)
    @Pattern(regexp = "([A-Za-z_]+)", message = "The name field must contains only A-Z, a-z or underscore symbols")
    @Size(min = 2, max = 25, message = " The name field must have min 2 symbols max 25 symbols ")
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @Column(name = "min_salary")
    @DecimalMin(value = "500", message = " The minSalary field must have a min value of 500 ")
    @DecimalMax(value = "1000000", inclusive = false,
            message = " The minSalary field must have a max value of 999999.99 ")
    public Double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    @Column(name = "max_salary")
    @DecimalMin(value = "500", message = " The maxSalary field must have a min value of 500 ")
    @DecimalMax(value = "1000000", inclusive = false,
            message = " The maxSalary field must have a max value of 999999.99 ")
    public Double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH}, mappedBy = "department")
    @JsonIgnore
//  @JsonIgnoreProperties("department")
    public List<Employee> getEmployee() {
        return employee;
    }

    public void setEmployee(List<Employee> employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Department that = (Department) o;
        return id == that.id && departmentName.equals(that.departmentName)
                && (Double.compare(minSalary, that.minSalary) == 0)
                && (Double.compare(maxSalary, that.maxSalary) == 0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentName, minSalary, maxSalary);
    }
}
