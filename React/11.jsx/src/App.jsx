import './App.css'

function Header() {
  return (
    <>
      <h2>Welcome to ReactJS</h2>
      <p>
        Today is <strong>26/07/2026</strong>. Currently is {" "} <strong>19:90</strong>
      </p>
    </>
  )
}

function App() {
  return (
    <>
      <h1>Đây là tiêu đề</h1>
      <p>Lorem ipsum dolor sit.</p>
      <Header/>
      <Header/>
    </>
  )
}

export default App
