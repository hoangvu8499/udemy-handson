import { useEffect, useRef, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import EditEmployee from "./EditEmployee";
import FilterEmployee from "./FilterEmployee";
import ExportEmployeePdf from "./ExportEmployeePdf";
import Pagination from "./Pagination";
import { isTokenValid } from "../utils/authUtils";
import { getAllCategories } from "../utils/categoryService"
import { useTranslation } from "react-i18next";

export default function ShowEmployee() {
    const navigation = useNavigate();
    const dialog = useRef();
    const [employeeId, setEmployeeId] = useState();
    const [message, setMessage] = useState("");
    const [filterObject, setFilterObject] = useState({
        empName:"",
        catId:""
    });
    const [sortField, setSortField] = useState("");
    const [direction, setDirection] = useState("");
    const [totalPages, setTotalPages] = useState(0);
    const [pageSize, setPageSize] = useState(0);
    const [pageNumber, setPageNumber] = useState(0);
    const [employees, setEmployees] = useState([]);
    const [error, setError] = useState("");
    const [categories, setCategories] = useState([]);
    const [file, setFile] = useState(null);
    const { t } = useTranslation();

    useEffect(() => {
        loadEmployees();
        loadCategories();
    }, []);

    const loadCategories = async () => {
        try {
            if(isTokenValid) {
                const response = await getAllCategories();
                setCategories(response.data);
            }
        } catch(error) {
            console.error(error);
        }
    }

    useEffect(() => { //pageNumber đổi thì load lại Employees
        loadEmployees();
    }, [pageNumber, sortField, direction]);

    const loadEmployees = async () => {
        try {
            if(isTokenValid()) {
                const response = await axios.post(
                    "http://localhost:8080/employees/searchEmployee",
                    filterObject,
                    {
                        params: {
                            page: pageNumber,
                            size: 5,
                            sortField: sortField,
                            direction: direction
                        },
                        // headers: {
                        //     Authorization: `Bearer ${localStorage.getItem("accessToken")}`
                        // },
                        timeout: 3000,
                        withCredentials: true
                    }
                );
                console.log("------loadEmployees: ", response.data.content);
                console.log("------pageNumber: ", pageNumber);
                console.log("------sortField: ", sortField);
                console.log("------direction: ", direction);
                setTotalPages(response.data.totalPages);
                setPageSize(response.data.pageable.pageSize);
                setPageNumber(response.data.pageable.pageNumber);
                setEmployees(response.data.content);
            } else {
                navigation('/login');
            }
            
        } catch (err) {
            console.log("ERROR =", error.code);
            if (error.code === "ERR_NETWORK") {
                console.log("Request timeout sau 3 giây");
                setMessage(
                   "API PROBLEM - PLEASE CHECK CONNECTION"
                );
            } else if (error.response) {
                console.log("STATUS =", error.response.status);
                console.log("DATA =", error.response.data);
                setError("Không thể tải danh sách nhân viên");
            } else {
                setMessage("Unknown error");
            }
        }
    };

    const handleSort = (sortField, sortDirection) => {
        setSortField(sortField);
        setDirection(sortDirection);
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
        setFilterObject((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSearch = () => {
        if(pageNumber>0) {
            setPageNumber(0);
        } else {
            loadEmployees();
        }
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
                timeout: 3000,
                withCredentials: true
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

    const handleFileUpload = (e) => {
        const selectedFile = e.target.files[0];
        if (!selectedFile) {
            return;
        }
        importEmployee(selectedFile);
    };


    const importEmployee = async (selectedFile) => {
        try {
            const formData = new FormData();
            formData.append("file", selectedFile);
            const response = await axios.post(
                "http://localhost:8080/employees/import",
                formData,
                {
                    headers: {
                        "Content-Type": "multipart/form-data",
                    },
                    timeout: 3000,
                    withCredentials: true,
                    responseType: "blob"
                }
            );
            if (response.status === 200) {
                setMessage(
                   "Import employee thành công"
                );
                loadEmployees();
            }
        } catch (error) {

            if (error.response?.status === 400) {

                const blob = new Blob(
                    [error.response.data],
                    { type: "text/plain" }
                );

                const url = window.URL.createObjectURL(blob);

                const link = document.createElement("a");
                link.href = url;
                link.download = "employee_import_error.txt";

                document.body.appendChild(link);
                link.click();

                document.body.removeChild(link);
                window.URL.revokeObjectURL(url);
                setMessage("Import thất bại. Vui lòng kiểm tra file lỗi.");
                loadEmployees();
            } else {
                setMessage("Có sự cố xảy ra, vui lòng thử lại sau.");
                loadEmployees();
                console.error(error);
            }
        }
    };

    return (
      <>
        <EditEmployee employeeId={employeeId}  ref={dialog} 
                    onUpdateSuccess={(msg) => {
                      setMessage(msg);
                      loadEmployees();
                     }} />
        
        <div>
            <h2>{t("employeeManagement")}</h2>
            {message && <p className="message" style={{ color: 'red' }} >{message}</p>}
            <div
                style={{
                    display: "flex",
                    alignItems: "center",
                    justifyContent: "center",
                    gap: "12px",
                    padding: "12px",
                    marginBottom: "20px",
                    backgroundColor: "#f8f9fa",
                    borderRadius: "8px",
                    border: "1px solid #ddd",
                }}
            >
                <button
                    onClick={redirectToAddEmployee}
                    style={{
                        padding: "8px 16px",
                        backgroundColor: "#28a745",
                        color: "white",
                        border: "none",
                        borderRadius: "5px",
                        cursor: "pointer",
                        fontWeight: "bold",
                    }}
                >
                    {t("addEmployee")} 
                    {/* label multi language */}
                </button>

                <label
                    style={{
                        fontWeight: "bold",
                        color: "#555",
                    }}
                >
                    {t("importEmployee")} 
                </label>

                <input
                    type="file"
                    accept=".xlsx,.xls,.csv"
                    onChange={handleFileUpload}
                    style={{
                        padding: "6px",
                        border: "1px solid #ccc",
                        borderRadius: "5px",
                        backgroundColor: "white",
                    }}
                />

                <ExportEmployeePdf employees={employees} />

                {error && (
                    <span
                        style={{
                            color: "red",
                            fontWeight: "bold",
                        }}
                    >
                        {error}
                    </span>
                )}
            </div>

            <FilterEmployee filterObject={filterObject} 
            handleFilterChange={handleFilterChange} 
            handleSearch={handleSearch}
            setFilterObject={setFilterObject}
            categories={categories}/>

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
                        <th>
                            <div className="sortable-header">
                                <span>ID</span>
                                <div className="sort-icons">
                                    <span onClick={() =>handleSort("emp_id","ASC")}>▲</span>
                                    <span onClick={() =>handleSort("emp_id","DESC")}>▼</span>
                                </div>
                            </div>
                        </th>
                        <th>
                            <div className="sortable-header">
                                <span>Employee Name</span>
                                <div className="sort-icons">
                                    <span onClick={() =>handleSort("emp_name","ASC")}>▲</span>
                                    <span onClick={() =>handleSort("emp_name","DESC")}>▼</span>
                                </div>
                            </div>
                        </th>
                        <th>
                            <div className="sortable-header">
                                <span>Employee Address</span>
                                <div className="sort-icons">
                                    <span onClick={() =>handleSort("emp_address","ASC")}>▲</span>
                                    <span onClick={() =>handleSort("emp_address","DESC")}>▼</span>
                                </div>
                            </div>
                        </th>
                        <th>
                            <div className="sortable-header">
                                <span>Employee Salary</span>
                                <div className="sort-icons">
                                    <span onClick={() =>handleSort("emp_salary","ASC")}>▲</span>
                                    <span onClick={() =>handleSort("emp_salary","DESC")}>▼</span>
                                </div>
                            </div>
                        </th>
                        <th>
                            <div className="sortable-header">
                                <span>Category</span>
                                <div className="sort-icons">
                                    <span onClick={() =>handleSort("cat_id","ASC")}>▲</span>
                                    <span onClick={() =>handleSort("cat_id","DESC")}>▼</span>
                                </div>
                            </div>
                        </th>
                        <th>
                            <div className="sortable-header">
                                <span>Action</span>
                                <div className="sort-icons">
                                    <span>▲</span>
                                    <span>▼</span>
                                </div>
                            </div>
                        </th>
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

            <Pagination currentPage={pageNumber} totalPages={totalPages} 
                    pageNumbers = {pageSize} setPageNumber={setPageNumber}/>
        </div>
        </>
    );
    
}