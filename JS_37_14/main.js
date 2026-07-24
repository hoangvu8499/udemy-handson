const panels = document.querySelectorAll(".panel")
console.log(panels);

panels.forEach((item) => {
    item.addEventListener("click", () => {
        removeActiveClass();
        item.classList.add("active")
    })
})

function removeActiveClass() {
    panels.forEach((item) => {
        item.classList.remove("active")
    })
}