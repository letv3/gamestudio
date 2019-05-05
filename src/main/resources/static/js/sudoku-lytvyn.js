let val = 0;

function setCurrentValue(value) {
    val = value;
}

function mark(value) {
    location.href = location.protocol + '//' + location.host + '/sudoku-lytvyn/' + val + '/' + value;
}
// function refreshScore() {
//     $.ajax({url: "/rest/score/sudoku",}).done(function (json) {
//         $("#scoreList").empty();
//         for (var i in json) {
//             var score = json[i];
//             $("#scoreList").append($("<li>" + score.player + " " + score.points + " " + score.playedOn + "</li>"));
//         }
//     });
// }
