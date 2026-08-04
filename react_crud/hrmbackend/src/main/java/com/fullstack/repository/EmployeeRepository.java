package com.fullstack.repository;

import com.fullstack.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
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
        LEFT JOIN category c
            ON e.cat_id = c.cat_id
        """,
        nativeQuery = true)
    List<Object[]> findEmployeesWithCategory();
}

}
