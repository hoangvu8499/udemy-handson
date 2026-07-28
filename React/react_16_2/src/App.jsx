import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'

function App() {
  const [user, setUser] = useState({fullName:"", email:""});

  const handleValue = (event) => {
    const {name, value} = event.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }))
  }

  return (<>
    <label>
      Input your full name: 
      <input type="text" name="fullName" value={user.fullName} onChange={handleValue} />
    </label>
    <label>
      Input your email: 
      <input type="text" name="email" value={user.email} onChange={handleValue} />
    </label>
    
    <h3>Show information</h3>
    <label>
      Your full name:  <p>{user.fullName}</p>
    </label>
    <label>
      Your email:  <p>{user.email}</p>
    </label>
    
  </>)
}

export default App
