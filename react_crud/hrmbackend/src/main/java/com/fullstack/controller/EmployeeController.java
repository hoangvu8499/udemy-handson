package com.fullstack.controller;

import com.fullstack.dto.EmployeeDTO;
import com.fullstack.entity.Employee;
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
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/save")
    public ResponseEntity<Employee> save(@Valid @RequestBody Employee employee) {
        log.info("@@@@@@Trying to save data for Employee: " + employee.getEmpName());
        return ResponseEntity.ok(employeeService.save(employee));
    }

    @GetMapping("/findbyid/{empId}")
    public ResponseEntity<EmployeeDTO> findById(@PathVariable int empId) {
        return ResponseEntity.ok(employeeService.findEmployeesWithCategoryById(empId));
    }

    @GetMapping("/findall")
    public ResponseEntity<List<Employee>> findAll() {
        return ResponseEntity.ok(employeeService.findAll());
    }

    @PostMapping("/searchEmployee")
    public ResponseEntity<List<EmployeeDTO>> searchEmployee(@RequestBody EmployeeDTO employee) {
        return ResponseEntity.ok(employeeService.findAllEmployees(employee));
    }

    @PutMapping("/update/{empId}")
    public ResponseEntity<Employee> update(@PathVariable int empId, @RequestBody Employee employee) {
        return ResponseEntity.ok(employeeService.update(empId, employee));
    }

    @DeleteMapping("/deletebyid/{empId}")
    public ResponseEntity<String> deleteById(@PathVariable int empId) {
        employeeService.deleteById(empId);
        return ResponseEntity.ok("Data Deleted Successfully");
    }
}
