package com.fullstack.service;

import com.fullstack.entity.Category;
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

    public Optional<Category> findById(int catId) {
        return Optional.ofNullable(categoryRepository.findById(catId).orElseThrow(() -> new RuntimeException("Category #ID Does Not Exist")));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

}
