package com.fullstack.service;

import com.fullstack.entity.Employee;
import com.fullstack.repository.CategoryRepository;
import com.fullstack.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Optional<Employee> findById(int empId) {
        return Optional.ofNullable(categoryRepository.findById(empId).orElseThrow(() -> new RuntimeException("Category #ID Does Not Exist")));
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

}
