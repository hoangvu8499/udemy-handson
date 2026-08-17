import { useState } from "react";
import axios from "axios";
import { redirect, useNavigate } from 'react-router-dom';
import { isTokenValid } from "../utils/authUtils";
import { jwtDecode } from "jwt-decode";

function Login() {

    const [loginForm, setLoginForm] = useState({
        phoneNumber: "",
        password: ""
    });
    const navigation = useNavigate();

    const [errMessage, setErrMessage] = useState("");

    const handleChange = (e) => {
        setLoginForm({
            ...loginForm,
            [e.target.name]: e.target.value
        });
    };

    const handleLogin = async () => {
        try {
            const response = await axios.post(
                "http://localhost:8080/api/auth/login2",
                loginForm,
                {
                    timeout: 3000,
                    withCredentials: true
                }
            );

            console.log("Response:", response.data);

            const tokenData = response.data.data;
            const decoded = jwtDecode(tokenData.accessToken);
            localStorage.setItem("expiresTime", decoded.exp);
            // localStorage.setItem("accessToken", tokenData.accessToken);
            // localStorage.setItem("refreshToken", tokenData.refreshToken);
            // localStorage.setItem("tokenType", tokenData.tokenType);
            // localStorage.setItem("expiresInSeconds", tokenData.expiresInSeconds);

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

    };

    return (
        <>
            <div className='col-md-9 offset-md-3 border rounded p-4 mt-2 shadow'>
                        {errMessage && <p className="message" style={{ color: 'red' }} >{errMessage}</p>}
                        <div className='mb-3'>
                            phoneNumber <input
                                            type="text"
                                            name="phoneNumber"
                                            value={loginForm.phoneNumber}
                                            onChange={handleChange}
                                        />
                        </div>

                        <div className='mb-3'>
                            Password  <input
                                        type="password"
                                        name="password"
                                        value={loginForm.password}
                                        onChange={handleChange}
                                    />
                        </div>
                        

                         <button onClick={handleLogin}>
                            Login
                        </button>
                </div>
            
        
           
        </>
    );
}

export default Login;