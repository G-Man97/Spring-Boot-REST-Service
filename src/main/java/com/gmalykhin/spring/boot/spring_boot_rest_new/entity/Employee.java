package com.gmalykhin.spring.boot.spring_boot_rest_new.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.time.LocalDate;
import java.util.Objects;


@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {

    @Column(name = "name")
    @Pattern(regexp = "([A-Za-z]+)", message = " The name field must contains only A-Z or a-z symbols ")
    @Size(min = 2, max = 25, message = " The name field must have min 2 symbols max 25 symbols ")
    private String name;

    @Column(name = "surname")
    @Pattern(regexp = "([A-Za-z]+)", message = " The surname field must contains only A-Z or a-z symbols ")
    @Size(min = 2, max = 25, message = " The surname field must have min 2 symbols max 25 symbols ")
    private String surname;

    @Column(name = "birthday")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Past(message = " The birthday field must contain the past date ")
    private LocalDate birthday;

    @Column(name = "salary")
    @DecimalMin(value = "500", message = " The salary field must have a min value of 500 ")
    @DecimalMax(value = "1000000", inclusive = false,
            message = " The salary field must have a max value of 999999.99 ")
    private Double salary;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "department_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @JsonIgnoreProperties({"minSalary", "maxSalary"})
    private Department department;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return name.equals(employee.name)
                && surname.equals(employee.surname)
                && birthday.isEqual(employee.birthday)
                && (Double.compare(salary, employee.salary) == 0)
                && ((department == null && employee.department == null)
                        || department.equals(employee.department));
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, birthday, salary, department);
    }
}
