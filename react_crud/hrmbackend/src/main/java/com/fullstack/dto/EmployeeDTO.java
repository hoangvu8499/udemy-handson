package com.fullstack.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private Integer empId;
    private String empName;
    private String empAddress;
    private Double empSalary;
    private Integer catId;
    private String catName;
}