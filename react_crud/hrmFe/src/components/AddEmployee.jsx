import axios from "axios";
import { useState } from "react"
import { redirect, useNavigate } from 'react-router-dom';

export default function AddEmployee() {

    const navigation = useNavigate();

    const [employee, setEmployee] = useState({
        empName:"",
        empAddress:"",
        empSalary:""
    });

    const [errMessage, setErrMessage] = useState("");

    const {empName, empAddress, empSalary} = employee;

    const onInputChange = (e) => {
        setEmployee({...employee, [e.target.name]: e.target.value})
    }

    const onSubmit = async(e) => {

        e.preventDefault()
        setErrMessage(null);
        if (!employee.empName?.trim()) {
            setErrMessage("Employee Name can not Empty");
            return;
        }

        if (!employee.empAddress?.trim()) {
            setErrMessage("Employee Address can not Empty");
            return;
        }

        if (employee.empSalary == null || employee.empSalary === '') {
            setErrMessage("Employee Salary can not Empty");
            return;
        }

        try {
            const response = await axios.post(
                "http://localhost:8080/employees/save",
                employee,
                {
                    timeout: 3000 // 3 giây
                }
            );

            console.log("Response:", response.data);
            navigation('/');
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
                   "Have problem when save Employee information | Error code: "+ error.response.status +" Message: "+error.response.data.message
                );
            } else {
                setErrMessage("Unknown error");
            }
        }
        
    }

    const redirectToShowEmployee = async () => {
        try {
          navigation('/');
        } catch (err) {
            console.error(err);
            setError("Không thể tải màn hình ShowEmployee");
        }
    };

    return (
        <>
            <div className='container'>

            <div className='row'>

                <div className='col-md-9 offset-md-3 border rounded p-4 mt-2 shadow'>
                    <form onSubmit={(e) => onSubmit(e)}>
                        {errMessage && <p className="message" style={{ color: 'red' }} >{errMessage}</p>}
                        <div className='mb-3'>
                            Name<input type='text' name='empName' value={empName} onChange={(e) => onInputChange(e)} />
                        </div>

                        <div className='mb-3'>
                            Address<input type='text' name='empAddress' value={empAddress} onChange={(e) => onInputChange(e)} />
                        </div>

                        <div className='mb-3'>
                            Salary<input type='number' name='empSalary' value={empSalary} onChange={(e) => onInputChange(e)} />
                        </div>

                        <button type='submit' className='btn btn-success'>Add Employee</button>
                        <button className='btn btn-success' style={{marginLeft: '5px'}} onClick={redirectToShowEmployee}>BACK</button>

                    </form>
                </div>

            </div>

        </div>
        </>
    )
}
