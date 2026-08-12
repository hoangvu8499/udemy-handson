package com.fullstack.service;

import com.fullstack.dto.EmployeeDTO;
import com.fullstack.entity.Employee;
import com.fullstack.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Optional<Employee> findById(int empId) {
        return Optional.ofNullable(employeeRepository.findById(empId).orElseThrow(() -> new RuntimeException("Employee #ID Does Not Exist")));
    }


    public EmployeeDTO findEmployeesWithCategoryById(int empId) {

        List<Object[]> rows = employeeRepository.findEmployeesWithCategoryById(empId);

        for (Object[] row : rows) {
            EmployeeDTO dto = new EmployeeDTO();

            dto.setEmpId((Integer) row[0]);
            dto.setEmpName((String) row[1]);
            dto.setEmpAddress((String) row[2]);
            dto.setEmpSalary((Double) row[3]);
            dto.setCatId((Integer) row[4]);
            dto.setCatName((String) row[5]);

            return dto;
        }

        return null;
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee update(int empId, Employee employee) {
        Employee employee1 = findById(empId).get();

        employee1.setEmpName(employee.getEmpName());
        employee1.setEmpAddress(employee.getEmpAddress());
        employee1.setEmpSalary(employee.getEmpSalary());

        return employeeRepository.save(employee1);
    }


    public void deleteById(int empId) {
        employeeRepository.deleteById(empId);
    }

    public Page<EmployeeDTO> findAllEmployees(EmployeeDTO employee, int page, 
        int size, String sortField, String direction) {
        Sort sort = null;
        if(!StringUtils.isEmpty(sortField) && !StringUtils.isEmpty(direction)) {
            sort = Sort.by(
                Sort.Direction.fromString(direction),
                sortField
            );
        }
        Pageable pageable = null;
        if(sort != null) {
            pageable = PageRequest.of(page, size, sort);
        } else {
            pageable = PageRequest.of(page, size);
        }
        Page<Object[]> rows = employeeRepository.findEmployeesWithCategory(
                employee.getCatId(),
                employee.getEmpName(),
                pageable);

        return rows.map(row -> {
            EmployeeDTO dto = new EmployeeDTO();

            dto.setEmpId((Integer) row[0]);
            dto.setEmpName((String) row[1]);
            dto.setEmpAddress((String) row[2]);
            dto.setEmpSalary((Double) row[3]);
            dto.setCatId((Integer) row[4]);
            dto.setCatName((String) row[5]);

            return dto;
        });
    }
}
