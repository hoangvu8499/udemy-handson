import React, { useEffect, useState } from 'react'
import axios from "axios";
import { redirect, useNavigate } from 'react-router-dom';

export default function EditEmployee({
  employeeId,
  ref,
  onUpdateSuccess
}) {
  const navigation = useNavigate();
  const [errMessage, setErrMessage] = useState("");
  const [employeeEdited, setEmployeeEdited] = useState({
          empId:"",
          empName:"",
          empAddress:"",
          empSalary:""
      });

  useEffect(() => {
    async function loadEmployee() {
      const response = await axios.get(
        `http://localhost:8080/employees/findbyid/${employeeId}`,
        {
          timeout: 3000,
          withCredentials: true
        }
      );

      setEmployeeEdited(response.data);
    }

    if (employeeId) {
      loadEmployee();
    }
  }, [employeeId]);

  
  const onInputChange = (e) => {
        setEmployeeEdited({...employeeEdited, [e.target.name]: e.target.value})
  }

  const onSubmit = async(e) => {

        e.preventDefault()
        if (!employeeEdited.empName?.trim()) {
            setErrMessage("Employee Name can not Empty");
            return;
        }

        if (!employeeEdited.empAddress?.trim()) {
            setErrMessage("Employee Address can not Empty");
            return;
        }

        if (employeeEdited.empSalary == null || employeeEdited.empSalary === '') {
            setErrMessage("Employee Salary can not Empty");
            return;
        }
        
        console.log("Address: "+employeeEdited.empAddress);
        console.log("NAME: "+employeeEdited.empName);
        console.log("SALARY: "+employeeEdited.empSalary);
        console.log("empId: "+employeeEdited.empId);

        try {
            const response = await axios.put(
                `http://localhost:8080/employees/update/${employeeEdited.empId}`,
                employeeEdited,
                {
                  timeout: 3000,
                  withCredentials: true
                }
            );

            console.log("Response:", response.data);
            setErrMessage("UPDATED SUCCESS")
            // refresh data ở component cha
            onUpdateSuccess?.(`Employee ${employeeEdited.empName} updated successfully`);
            // đóng dialog
            ref.current?.close();
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
                   "Have problem when UPdate Employee information | Error code: "+ error.response.status +" Message: "+error.response.data.message
                );
            } else {
                setErrMessage("Unknown error");
            }
        }
        
    }
  
  return (
    <dialog ref={ref} >
      <h3>Edit Employee</h3>
      {errMessage && <p className="message" style={{ color: 'red' }} >{errMessage}</p>}
      <form onSubmit={(e) => onSubmit(e)} >
        <p>
          Name: <strong><input type='text' name='empName' value={employeeEdited?.empName} onChange={(e) => onInputChange(e)} /></strong>
        </p>

        <p>
          Address: <strong><input type='text' name='empAddress' value={employeeEdited?.empAddress} onChange={(e) => onInputChange(e)} /></strong>
        </p>

        <p>
          Salary: <strong><input type='text' name='empSalary' value={employeeEdited?.empSalary} onChange={(e) => onInputChange(e)} /></strong>
        </p>
        <button type='submit' className='btn btn-success'>Edit Employee</button>
      </form>
      
    </dialog>
  );
}