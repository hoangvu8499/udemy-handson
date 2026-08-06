import { useEffect, useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import EditEmployee from "./EditEmployee";

export default function ShowEmployee({
        categories
    }) {
    const navigation = useNavigate();
    const dialog = useRef();
    const [employeeId, setEmployeeId] = useState();
    const [message, setMessage] = useState("");
    const [filterObject, setFilterObject] = useState({
        empName:"",
        catId:""
    });


    const [employees, setEmployees] = useState([]);
    const [error, setError] = useState("");
    // const [categories, setCategories] = useState([]);

    useEffect(() => {
        loadEmployees();
        // loadCategories();
    }, []);

    useEffect(() => {
        console.log("filterObject =", filterObject);
    }, [filterObject]);

    // const loadCategories = async () => {
    //     try {
    //         const response = await axios.get(
    //             "http://localhost:8080/category/findall"
    //         );

    //         setCategories(response.data);
    //     } catch (error) {
    //         console.error(error);
    //     }
    // };

    const loadEmployees = async () => {
        try {

            const response = await axios.post(
                "http://localhost:8080/employees/searchEmployee",
                filterObject,
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

    const handleFilterChange = (e) => {
        const { name, value } = e.target;

        console.log("1--:", e.target);
        console.log("1--:", name);
        console.log("1--:", value);

        setFilterObject((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSearch = () => {
        loadEmployees();
    };

    function handleEdit(employeeId) {
      setEmployeeId(employeeId);
      dialog.current.showModal();
    }

    async function handleDeleteEmployee(employeeId) {
        try {
            const response = await axios.delete(
              `http://localhost:8080/employees/deletebyid/${employeeId}`,
              {
                timeout: 3000
              }
            );

            console.log("Response:", response.data);
            setMessage("DELETE SUCCESS")
            loadEmployees();
        } catch (error) {
            console.log("ERROR =", error.code);
            if (error.code === "ERR_NETWORK") {
                console.log("Request timeout sau 3 giây");
                setErrMessage(
                   "API PROBLEM - PLEASE CHECK CONNECTION"
                );
            } else if (error.response) {
                console.log("STATUS =", error.response.status);
                console.log("DATA =", error.response.data);
                setErrMessage(
                   "Have problem when DELETE Employee information | Error code: "+ error.response.status +" Message: "+error.response.data.message
                );
            } else {
                setErrMessage("Unknown error");
            }
        }
    }


    return (
      <>
        <EditEmployee employeeId={employeeId}  ref={dialog} 
                    onUpdateSuccess={(msg) => {
                      setMessage(msg);
                      loadEmployees();
                     }} />
        <div>
            <h2>Employee List</h2>
            {message && <p className="message" style={{ color: 'red' }} >{message}</p>}
            <button onClick={redirectToAddEmployee}>Add Employee</button>
            {error && (
                <p style={{ color: "red" }}>
                    {error}
                </p>
            )}

            <div style={{ margin: "15px 0" }}>
              <input
                  type="text"
                  name="empName"
                  placeholder="Employee Name"
                  value={filterObject.empName}
                  onChange={handleFilterChange}
                  style={{ marginRight: "10px" }}
              />

              <select
                  name="catId"
                  value={filterObject.catId}
                  onChange={handleFilterChange}
                  style={{ marginRight: "10px" }}
              >
                  <option value="">All Category</option>

                  {categories.map((category) => (
                      <option
                          key={category.catId}
                          value={category.catId}
                      >
                          {category.catName}
                      </option>
                  ))}
              </select>

              <button onClick={handleSearch}>
                  Search
              </button>

              <button
                  style={{ marginLeft: "10px" }}
                  onClick={() => {
                      setFilterObject({
                          empName: "",
                          catId: ""
                      });
                  }}
              >
                  Clear
              </button>
            </div>

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
                        <th>Category</th>
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
                            <td>{employee.catName}</td>
                            <td>
                                <button onClick={() => handleEdit(employee.empId)}>
                                  Edit
                                </button>
                                &nbsp;
                                <button onClick={() => handleDeleteEmployee(employee.empId)}>
                                  Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
        </>
    );
    
}