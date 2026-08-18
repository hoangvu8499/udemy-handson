package com.fullstack.controller;

import com.fullstack.dto.EmployeeDTO;
import com.fullstack.entity.Employee;
import com.fullstack.service.EmployeeService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;

import javax.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
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
    public ResponseEntity<Page<EmployeeDTO>> searchEmployee(
            @RequestBody EmployeeDTO employee,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "") String sortField,
            @RequestParam(defaultValue = "") String direction
    ) {
        log.info("----sortField------" + sortField);
        log.info("----direction------" + direction);
        return ResponseEntity.ok(employeeService.findAllEmployees(employee, page, size, sortField, direction));
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

    @PostMapping(value ="/import",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Resource> importEmployee(
            @RequestParam("file") MultipartFile file) throws Exception {
        List<String> errors = employeeService.importEmployee(file);
        if (!errors.isEmpty()) {
            String content = String.join("\n", errors);
            ByteArrayResource resource = new ByteArrayResource(content.getBytes());
            return ResponseEntity.badRequest()
                    .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=employee_import_error.txt"
                    )
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(resource);
        }
        return ResponseEntity.ok().build();
    }
}
