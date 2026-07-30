import { useEffect, useState } from "react"
import TimeDemo from "./TimeDemo";
import ResizeEventDemo from "./ResizeEventDemo";

export default function Parent() {
    const [show, setShow] = useState(true);
    
    return (
        <>
            <button onClick={() => setShow((s) => !s)}>{show ? "Ẩn components" : "Hiện components"} </button>
            {show && <ResizeEventDemo />}
        </>
    )
}