import './App.css'
import pathImage from "../src/assets/img/rupac.jpg";

let today = new Date().toLocaleDateString();
let time = new Date().toLocaleTimeString();

const wellcome = [
  "TITLE ABCDddddddddddddddd",
  "TITLE ggggggggggggggggggg",
  "TITLE ppppppppppppppppppp"
]
function randomTitle() {
  return Math.floor(Math.random() * wellcome.length);
}

function Header() {
  const title = wellcome[randomTitle()];
  return (
    <>
      <h1>{title}</h1>
      <p>To day is: <strong>{today}</strong>. Currently is {" "}<strong>{time}</strong></p>
    </>
  )
}

const name = "Timo";
let isLogedIn = true;

function DinamicValue() {
  return (
    <>
      <h2>Hola {name}</h2>
      <p>Kết quả: {2*2} </p>
      <p>{isLogedIn ?"LogedIn":"Please login"}</p>

      <img src={pathImage}/>
    </>
  );
}

function App() {
  return (
    <>
     <Header/>
     <DinamicValue/>
    </>
  )
}

export default App
