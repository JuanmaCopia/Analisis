
/*
* Establish the WebSocket connection and set up event handlers.
*/
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/lobbyy");

/*
* WebSocket Message Handler: Executes a set of functions depending on the msg.
* parameters:
*     - msg (string): json object in string format, it contains the task identifier and the needed data.
*/
webSocket.onmessage = function (msg) {
    var data = JSON.parse(msg.data);
    var task = data.task;
    if (match == null) {
        switch (task) {
            case "refreshTables":
                if (user == null)
                    setUser();
                if (sittedTable == null)
                    setTable();
                displayAllTables(data.tableList);
            break;
            case "displayCreatedTable":
                createTable(data.newTable);
                displayTable(data.newTable);
            break;
            case "error":
                displayError(data.errorMsg);
            break;
            case "tableDeleted":
                deleteTable(data.deletedTable);
                removeTableFromView(data.deletedTable);
            break;
            case "userJoined":
                if (data.table.guest_id == user.id) {
                    sittedTable = new Table(data.table.id,false);
                }
            break;
            case "userLeftTable":
                if (data.guest_id == user.id) {
                    sittedTable = null;
                }
            break;
            case "startMatch":
            if (data.guest_id == user.id) {
                var firstQuestion = data.questionsArray[0];
                match = new Match(data.match_id,data.questionsArray,data.guestName,data.ownerName,firstQuestion.id);
                displayQuestion(firstQuestion);
            }
            else {
                if (data.owner_id == user.id) {
                    var firstQuestion = data.questionsArray[0];
                    match = new Match(data.match_id,data.questionsArray,data.ownerName,data.guestName,firstQuestion.id);
                    displayQuestion(firstQuestion);
                    sendDeleteTable();
                }
            }
            break;
        }
    }
    else {
        if (task == "showResult") {
            match.currentQuestion++;
            if (!data.matchOver) {
                displayFeedback(data.isCorrect,data.userAnswer,data.correctAnswer);
                if (match.currentQuestion == 3) {
                    match.currentQuestion = 0;
                }
                var nextQuestion = match.questionsArray[match.currentQuestion];
                match.lastQuestionId = nextQuestion.id;
                setTimeout(function() {
                    displayQuestion(nextQuestion);
                },1600);
            }
            else {
                displayStatistics(data.userScore,data.opponentScore,match.thisUserName,match.opponentName);
            }
        }
    }
}

/*
* WebSocket onclose hadler: when the websocket conection is lost, shows an alert message to the user.
*/
webSocket.onclose = function () { alert("WebSocket connection closed") };
