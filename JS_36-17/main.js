// https://raw.githubusercontent.com/GaLaiLapTrinh/pokemon/main/img/1.png
let baseUrl = "https://raw.githubusercontent.com/GaLaiLapTrinh/pokemon/main/img/";

const container = document.getElementById("container");

for(let i=1; i<=150; i++) {
    const newDiv = document.createElement("div")
    const parentDiv = container.appendChild(newDiv)

    const newImage = document.createElement("img")
    newImage.src=`${baseUrl}${i}.png`
    parentDiv.appendChild(newImage)
    
    const newSpan = document.createElement("span")
    newSpan.innerText=`#${i}`
    parentDiv.appendChild(newSpan)

}