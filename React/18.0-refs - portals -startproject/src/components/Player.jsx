import { useState, useRef } from "react"

export default function Player() {
    const playerName = useRef();
    const [enterPlayerName, setPlayerName] = useState(null);
    
    function submitValue() {
        setPlayerName(playerName.current.value);
    }

    return (
        <>
            <section id="player">
                <h2>Welcome {enterPlayerName ?? "MyFriend"} to this game</h2>
                <div>
                    <input type="text" name="playerName" ref={playerName} />
                    <button onClick={submitValue}>Submit</button>
                </div>
            </section>
        </>
    )
}