import { useEffect, useState } from "react"

export default function TimeDemo() {
    const [count, setCount] = useState(0);
    useEffect(() => {
            const setIntervalId = setInterval(
                () => {
                    console.log("SET INTERVAL");
                    setCount((prevCount) => prevCount +1);
                }, 1000);
            
                //cleanup
                return ()=>{
                    clearInterval(setIntervalId);
                    console.log("CLEAR INTERVAL");
                }
        }, []);
    return (
        <>
            <h3> Mount / Unmount components</h3>
            <p>Count: {count}</p>
        </>
    )
}