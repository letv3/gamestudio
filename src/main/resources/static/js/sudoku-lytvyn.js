let val = 0;

function setCurrentValue(value) {
    val = value;
}

function mark(value) {
    location.href = location.protocol + '//' + location.host + '/sudoku-lytvyn/' + val + '/' + value;
}