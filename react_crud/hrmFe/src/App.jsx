import { useEffect, useRef, useState } from "react";
import axios from "axios";
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import {BrowserRouter as Router, Route,  Routes } from 'react-router-dom'
import ShowEmployee from './components/ShowEmployee'
import AddEmployee from './components/AddEmployee'
import 'bootstrap/dist/css/bootstrap.min.css'
function App() {
  const [count, setCount] = useState(0)
  const [categories, setCategories] = useState([]);

  useEffect(() => {
       loadCategories();
  }, []);

  const loadCategories = async () => {
        try {
            const response = await axios.get(
                "http://localhost:8080/category/findall",
                {
                    headers: {
                        Authorization: `Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI2IiwicGhvbmVOdW1iZXIiOiIwODM0MzE0NDE0IiwianRpIjoiMjk3MzMwNmMtMmZkYS00MWU1LTllODEtYmIzNmE0OWIwZTU3IiwiaWF0IjoxNzg2NjEyODA4LCJleHAiOjE3ODY2MTY0MDh9.De3pQFS1qKVyZGtc_VdMPsoe0Pvpza9BDgbVYHlAfdo`
                    },
                    timeout: 3000
                }
            );

            setCategories(response.data);
        } catch (error) {
            console.error(error);
        }
  };

  return (
    <>
      <Router>
        <Routes>
          <Route  exact path='/' element={<ShowEmployee categories={categories}/>} /> 
          <Route  exact path='/addemployee' element={<AddEmployee categories={categories}/>} /> 
          {/* <Route exact path='/edit/:empId' element={<EditEmployee/>} />  */}

        </Routes>
      </Router>
    </>
  )
}

export default App
