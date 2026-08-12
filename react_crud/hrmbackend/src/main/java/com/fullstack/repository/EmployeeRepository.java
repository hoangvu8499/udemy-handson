package com.fullstack.repository;

import com.fullstack.dto.EmployeeDTO;
import com.fullstack.entity.Employee;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

        @Query(value = """
        SELECT
            e.emp_id,
            e.emp_name,
            e.emp_address,
            e.emp_salary,
            e.cat_id,
            c.cat_name
        FROM employee e
        INNER JOIN category c
            ON e.cat_id = c.cat_id
        WHERE 1 = 1
            AND (:catId IS NULL OR e.cat_id = :catId)
            AND (:empName IS NULL OR LOWER(e.emp_name) LIKE LOWER(CONCAT('%', :empName, '%')))
        """,
        countQuery = """
        SELECT COUNT(*)
        FROM employee e
        INNER JOIN category c
            ON e.cat_id = c.cat_id
        WHERE 1 = 1
            AND (:catId IS NULL OR e.cat_id = :catId)
            AND (:empName IS NULL OR LOWER(e.emp_name) LIKE LOWER(CONCAT('%', :empName, '%')))
        """,
        nativeQuery = true)
    Page<Object[]> findEmployeesWithCategory(
            @Param("catId") Integer catId,
            @Param("empName") String empName,
            Pageable pageable);

    @Query(value = """
    SELECT
        e.emp_id,
        e.emp_name,
        e.emp_address,
        e.emp_salary,
        e.cat_id,
        c.cat_name
    FROM employee e
    INNER JOIN category c
        ON e.cat_id = c.cat_id
    WHERE 1 = 1
        AND e.emp_id = :empId
    """,
            nativeQuery = true)
    List<Object[]> findEmployeesWithCategoryById(
            @Param("empId") Integer empId);        

}
