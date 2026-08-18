package com.fullstack.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EMPLOYEE")
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int empId;

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, message = "Employee name must be at least 2 characters")
    private String empName;

    private String empAddress;

    private double empSalary;

    private int catId;

    @Transient
    private Category category;
}
