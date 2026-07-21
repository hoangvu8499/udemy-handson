// const button1 = document.getElementById("button1");
// const titlePage = document.getElementById("titlePage");

// function changeColor() {
//     const r = Math.floor(Math.random()*256);
//     const g = Math.floor(Math.random()*256);
//     const b = Math.floor(Math.random()*256);
//     const color = `rgb(${r},${g},${b})`;

//     document.body.style.backgroundColor=`${color}`;
//     titlePage.textContent = `${color}`;
// }

// button1.addEventListener("click", changeColor);

const btns = document.querySelectorAll(".btn");

function randomColors() {
    const r = Math.floor(Math.random()*256);
    const g = Math.floor(Math.random()*256);
    const b = Math.floor(Math.random()*256);
    const color = `rgb(${r},${g},${b})`;

    return color;
}

function changeColor() {
    this.style.backgroundColor=randomColors();
}


for(let btn of btns) {
    if(btn instanceof HTMLElement) {
        btn.addEventListener("click",changeColor)
    }
}



