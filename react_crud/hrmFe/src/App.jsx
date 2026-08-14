import { useEffect, useRef, useState } from "react";
import axios from "axios";
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import {BrowserRouter as Router, Route,  Routes} from 'react-router-dom'
import ShowEmployee from './components/ShowEmployee'
import AddEmployee from './components/AddEmployee'
import 'bootstrap/dist/css/bootstrap.min.css'
import Login from "./components/Login";
import { isTokenValid } from "./utils/authUtils";

function App() {
  const [count, setCount] = useState(0)

  return (
    <>
      <Router>
        <Routes>
          <Route  exact path='/' element={<ShowEmployee />} /> 
          <Route  exact path='/addemployee' element={<AddEmployee />} />
          {/* <Route exact path='/edit/:empId' element={<EditEmployee/>} />  */}

          <Route  exact path='/login' element={<Login />} /> 

        </Routes>
      </Router>
    </>
  )
}

export default App
