import { useState, useRef } from "react"
import ResultDialog from "./ResultDialog";

export default function TimeStopper({title, seconds}) {
    const [isActive, setIsActive] = useState(false);
    const [score, setScore] = useState(0);
    const dialog = useRef();
    const refTime = useRef();
    let startTime = useRef();
    let currentTime = useRef();
    const [resultMesage, setResultMesage] = useState("You Lose");
    function startCheckTime() {
        if(isActive) {
            clearTimeout(refTime.current);
            setIsActive(false);
            return;
        }
        setIsActive(true);
        startTime.current = Date.now();
        refTime.current = setTimeout(() => {
                    currentTime.current = Date.now();
                    setScore(0);
                    setResultMesage("You Lose");
                    dialog.current.showModal();
                    setIsActive(false);
            }, seconds * 1000);
        
    }
    
    function handleStop() {
        if(isActive) {
            currentTime.current = Date.now();
            const resultTime = (currentTime.current-startTime.current)/1000;
            if(seconds===1) {
                setScore(resultTime*100);
            } else {
                setScore(Math.floor((resultTime/seconds)*100));
            }
            setResultMesage("You win");
            dialog.current.showModal();
            clearTimeout(refTime.current);
            setIsActive(false);
            return;
        }
    }

    return (
        <>
            <ResultDialog result={resultMesage} scores={score} targetTime={seconds} 
            currentTime={(currentTime.current-startTime.current)/1000} ref={dialog}/>
            <section className="challenge">
                <h3>Lever {title} </h3>
                {/* {isLost && <p>You lose</p>} */}
                <p className="challengeTime">{seconds} second{seconds > 1 ? "s":""}</p>
                {!isActive && <button onClick={startCheckTime}>Start</button>}
                {isActive && <button onClick={handleStop}>Stop</button>}
                <p>{isActive ? "Time is running..." : "Timer inactive"}</p>
            </section>
        </>
    )
}