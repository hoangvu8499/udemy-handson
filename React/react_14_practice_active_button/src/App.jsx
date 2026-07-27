
import './App.css'
import { useState } from 'react'
function App() {
  const [selectedActiveStatus, setSelectedActiveStatus] = useState(true);
  const [selectedWarning, setSelectedWarningStatus] = useState(false);
  const [selectedModeStatus, setSelectedModeStatus] = useState(false);

  function handleActionButton() {
    setSelectedActiveStatus(false);
    setSelectedWarningStatus(true);
  }

  function handleConfirmButton() {
    setSelectedModeStatus(true);
    setSelectedWarningStatus(false);
  }

  function handleCancelButton() {
    setSelectedActiveStatus(true);
    setSelectedWarningStatus(false);
  }

  return (
    <>
      <div className="container">
        {selectedWarning && 
          (<div className="warning-box">
            <p>Warning! Are you sure you want to activate this mode?</p>
            <button onClick={handleConfirmButton} className="confirm-btn">Confirm</button>
            <button onClick={handleCancelButton} className="cancel-btn">Cancel</button>
          </div>)
        }

        {selectedModeStatus && 
          (<div className="activated-box">
            Mode Activated!
          </div>)
        }

        {selectedActiveStatus && 
          (<div className="cancel-box">
            <button onClick={handleActionButton} className="activate-btn">Activate</button>
          </div>)
        }
        
      </div>
    </>
  )
}

export default App
