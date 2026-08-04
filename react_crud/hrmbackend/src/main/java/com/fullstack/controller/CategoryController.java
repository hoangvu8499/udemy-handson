package com.fullstack.controller;

import com.fullstack.entity.Category;
import com.fullstack.entity.Employee;
import com.fullstack.service.CategoryService;
import com.fullstack.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/findbyid/{catId}")
    public ResponseEntity<Optional<Category>> findById(@PathVariable int catId) {
        return ResponseEntity.ok(categoryService.findById(catId));
    }

    @GetMapping("/findall")
    public ResponseEntity<List<Category>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    
}
