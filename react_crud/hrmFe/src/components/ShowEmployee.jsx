import { useEffect, useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import EditEmployee from "./EditEmployee";

export default function ShowEmployee() {
    const navigation = useNavigate();
    const dialog = useRef();
    const [employeeSelected, setEmployeeSelected] = useState();


    const [employees, setEmployees] = useState([]);
    const [error, setError] = useState("");

    useEffect(() => {
        loadEmployees();
    }, []);

    const loadEmployees = async () => {
        try {
            const response = await axios.get(
                "http://localhost:8080/employees/findall",
                {
                    timeout: 3000
                }
            );

            setEmployees(response.data);
        } catch (err) {
            console.error(err);
            setError("Không thể tải danh sách nhân viên");
        }
    };

    const redirectToAddEmployee = async () => {
        try {
          navigation('/addEmployee');
        } catch (err) {
            console.error(err);
            setError("Không thể tải màn hình addEmployee");
        }
    };

    function handleEdit(employee) {
      setEmployeeSelected(employee)
      dialog.current.showModal();
    }

    return (
      <>
        <EditEmployee employee={employeeSelected}  ref={dialog} />
        <div>
            <h2>Employee List</h2>
            <button onClick={redirectToAddEmployee}>Add Employee</button>
            {error && (
                <p style={{ color: "red" }}>
                    {error}
                </p>
            )}

            <table
                border="1"
                cellPadding="10"
                style={{
                    borderCollapse: "collapse",
                    width: "100%"
                }}
            >
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Employee Name</th>
                        <th>Employee Address</th>
                        <th>Employee Salary</th>
                        <th>Action</th>
                    </tr>
                </thead>

                <tbody>
                    {employees.map((employee) => (
                        <tr key={employee.empId}>
                            <td>{employee.empId}</td>
                            <td>{employee.empName}</td>
                            <td>{employee.empAddress}</td>
                            <td>{employee.empSalary}</td>
                            <td>
                                <button onClick={() => handleEdit(employee)}>
                                  Edit
                                </button>
                                &nbsp;
                                <button>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
        </>
    );
    
}