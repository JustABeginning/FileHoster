document.addEventListener("contextmenu", (e) => {
    e.preventDefault();

    return false;
});

var ctrlDown = false;
var shiftDown = false;

//
document.addEventListener("keydown", (e) => {
    let evt = e || window.event;

    let status = null;

    if (evt.ctrlKey || evt.metaKey)
        ctrlDown = true;
    if (evt.shiftKey)
        shiftDown = true;

    var evtCode = evt.key;
    if (evtCode === "F12")
        status = false;
    else if (ctrlDown && shiftDown && evtCode === "I" || ctrlDown && evtCode === "u" || evtCode === "q" || evtCode == "Q")
        status = false;
    else
        status = true;

    if (status === false)
        e.preventDefault();

    return status;
});

//
document.addEventListener("keyup", (e) => {
    let evt = e || window.event;

    if (evt.ctrlKey || evt.metaKey)
        ctrlDown = false;
    if (evt.shiftKey)
        shiftDown = false;

    return true;
});
