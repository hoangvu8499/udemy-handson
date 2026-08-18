package com.fullstack.service;

import com.fullstack.dto.EmployeeDTO;
import com.fullstack.entity.Employee;
import com.fullstack.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Sort;

import java.io.InputStream;
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

    public List<String> importEmployee(MultipartFile file) throws Exception {
        try (
            InputStream is = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(is)
        ) {
            Sheet sheet = workbook.getSheetAt(0);
            List<Employee> employees = new ArrayList<>();
            List<String> errList = new ArrayList<>();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                StringBuilder errMessage = new StringBuilder("");
                String empName = row.getCell(0).getStringCellValue();
                String empAddress = row.getCell(1).getStringCellValue();
                Cell salaryCell = row.getCell(2);
                Double empSalary = null;
                if (salaryCell != null) {
                    empSalary = salaryCell.getNumericCellValue();
                }
                Cell categoryCell = row.getCell(3);
                String empCategory = categoryCell != null
                        ? categoryCell.getStringCellValue()
                        : null;

                if(StringUtils.isBlank(empName) || StringUtils.isEmpty(empName)) {
                    if (errMessage.length() == 0) { // Hoặc errMessage.isEmpty() nếu dùng Java 15+
                        errMessage.append("Record in line ").append(i);
                    }
                    errMessage.append(" | Employee Name can not be null or empty ");
                }
                if(StringUtils.isNotBlank(empName) && empName.length() < 2) {
                    if (errMessage.length() == 0) { // Hoặc errMessage.isEmpty() nếu dùng Java 15+
                        errMessage.append("Record in line ").append(i);
                    }
                    errMessage.append(" | Employee Name should have length > 2 ");
                }

                if(empSalary == null || empSalary < 0.0) {
                    if (errMessage.length() == 0) { // Hoặc errMessage.isEmpty() nếu dùng Java 15+
                        errMessage.append("Record in line ").append(i);
                    }
                    errMessage.append(" | Employee Salary should be > 0 ");
                }

                if(StringUtils.isBlank(empCategory) || StringUtils.isEmpty(empCategory)) {
                    if (errMessage.length() == 0) {
                        errMessage.append("Record in line ").append(i);
                    }
                    errMessage.append(" | Employee Category can not empty ");
                }
                if(errMessage.isEmpty()) {
                    Employee employee = Employee.builder()
                    .empName(empName)
                    .empAddress(empAddress)
                    .empSalary(empSalary)
                    .catId("PSI".equals(empCategory) ? 1 : 2)
                    .build();

                    employees.add(employee);
                } else {
                    errList.add(errMessage.toString());
                }
            }
            if(errList.isEmpty()) {
                employeeRepository.saveAll(employees);
            }
            return errList;
        }
        
    }
}
