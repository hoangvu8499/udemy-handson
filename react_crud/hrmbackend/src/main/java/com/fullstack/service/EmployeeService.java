package com.fullstack.service;

import com.fullstack.entity.Employee;
import com.fullstack.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
