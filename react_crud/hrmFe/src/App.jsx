import { useState } from 'react'
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

  return (
    <>
      <Router>
        <Routes>
          <Route exact path='/' element={<ShowEmployee/>} /> 
          <Route exact path='/addemployee' element={<AddEmployee/>} /> 
          {/* <Route exact path='/edit/:empId' element={<EditEmployee/>} />  */}

        </Routes>
      </Router>
    </>
  )
}

export default App
