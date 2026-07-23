const form = document.querySelector("#form");
const ul = document.getElementById("listItem");

form.addEventListener("submit", function(e) {
    e.preventDefault();
    const userName = document.getElementById("userName").value;
    const message = document.getElementById("message").value;
    const newLi = document.createElement("li");
    newLi.textContent = userName + ": " + message;
    ul.append(newLi);

    form.reset();
});

ul.addEventListener("click", function(e) {
    const li = e.target;
    console.log(e);
    if(li.tagName==="LI") {
        li.remove();
    }
})
