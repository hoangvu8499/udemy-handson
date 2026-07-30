import { useEffect } from "react";


export default function ResizeEventDemo() {

    useEffect(()=>{
        const handleResize = () => {
            if(window.innerWidth <= 800) {
                console.log("Size < 800");
            }
        };

        // Listen event resize
        window.addEventListener("resize", handleResize);
        console.log("Already resize: ")

        return () => {
            window.removeEventListener("resize", handleResize);
            console.log("Already remove resize");
        };

    },[]);

    return (
        <>
            <h3>Resize lower 800px</h3>
        </>
    )
}
