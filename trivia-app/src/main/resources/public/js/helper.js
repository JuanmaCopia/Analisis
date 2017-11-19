

// HTML Tables Templates
var HTMLCompleteTable = '<div id="table%id%" class="table"><div class="status"><p id="statusText">Mesa Completa</p></div><div class="players"><div class="owner"><p id="ownerName">%owner%</p></div><div class="vsBox"><img id="vsimg" src="/images/vs.png"></div><div class="guest"><div class="guestBox"><p id="guestName">%guest%</p></div></div></div><div class="buttons"></div></div>';
var HTMLIncompleteTable = '<div id="table%id%" class="table"><div class="status"><p id="statusText">Esperando Jugador</p></div><div class="players"><div class="owner"><p id="ownerName">%owner%</p></div><div class="vsBox"><img id="vsimg" src="/images/vs.png"></div><div class="guest"><div class="guestBox"><button id="join" onclick="sendJoinTable(%id%)">Entrar</button></div></div></div><div class="buttons"></div></div>';
var HTMLUserIncompleteTable = '<div id="table%id%" class="table"><div class="status"><p id="statusText">Esperando Jugador</p></div><div class="players"><div class="owner"><p id="ownerName">%owner%</p></div><div class="vsBox"><img id="vsimg" src="/images/vs.png"></div><div class="guest"><div class="guestBox"><p id="guestName"></p></div></div></div><div class="buttons"><div class="singleButtonBox"><button id="delIncomplete" onclick="sendDeleteTable()">Eliminar</button></div></div></div>';
var HTMLUserCompleteTable = '<div id="table%id%" class="table"><div class="status"><p id="statusText">Mesa Completa</p></div><div class="players"><div class="owner"><p id="ownerName">%owner%</p></div><div class="vsBox"><img id="vsimg" src="/images/vs.png"></div><div class="guest"><div class="guestBox"><p id="guestName">%guest%</p><button id="kick" onclick="sendGuestLeft(%id%)">X</button></div></div></div><div class="buttons"><div class="leftButtonBox"><button id="deleteTable" onclick="sendDeleteTable()">Eliminar</button></div><div class="rightButtonBox"><button id="startGame" onclick="sendStartGame(%id%)">Comenzar</button></div></div></div>';
var HTMLCompleteTableAsGuest = '<div id="table%id%" class="table"><div class="status"><p id="statusText">Mesa Completa</p></div><div class="players"><div class="owner"><p id="ownerName">%owner%</p></div><div class="vsBox"><img id="vsimg" src="/images/vs.png"></div><div class="guest"><div class="guestBox"><p id="guestName">%guest%</p></div></div></div><div class="buttons"><div class="singleButtonBox"><button id="exit" onclick="sendGuestLeft(%id%)">Salir</button></div></div></div>';
// HTML Question Template
var HTMLquestion = '<div class="questionBox"><div class="question"><p>Categoria: <span id="category">%category%</span></p><p id="questionText">%question%</p></div><div class="options"><button id="option1" onclick="sendAnswer(1)">%op1%</button><button id="option2" onclick="sendAnswer(2)">%op2%</button><button id="option3" onclick="sendAnswer(3)">%op3%</button><button id="option4" onclick="sendAnswer(4)">%op4%</button></div></div>';
// HTML Statistics Template
var HTMLstatistics = '<div class="mainStat"><div class="stats"><div class="winnerStats"><h1 id="winnerName">%winner%</h1><p id="winnerScore">%winnerScore%</p><p id="winnerText">Winner!</p></div><div class="vs"><img id="vsimg" src="/images/vs.png"></div><div class="loserStats"><h1 id="loserName">%loser%</h1><p id="loserScore">%loserScore%</p><p id="loserText">Loser</p></div></div><form action="/lobby" method="get"><input id="returnLobby" type="submit" value="Volver a la sala"></form></div>';

/*
* Shows to this user if his answer was correct or incorrect.
* parameters:
*     - isCorrect (boolean): true if his anser was correct, otherwise false.
*     - userAnswer (int): number of the answered option.
*     - correctAnswer (int): number of the correct option.
*/
function displayFeedback(isCorrect,userAnswer,correctAnswer) {
    if (isCorrect) {
        $("#option"+userAnswer).css("background-color","#23d134");
        $(".questionBox").append('<h1 id="rightFeedback">CORRECTO!!</h1>');
    }
    else {
        $("#option"+userAnswer).css("background-color","#e80000");
        $("#option"+correctAnswer).css("background-color","#23d134");
        $(".questionBox").append('<h1 id="wrongFeedback">INCORRECTO</h1>');
    }
};

/*
* Shows the match statistics of both players.
* parameters:
*     - userScore (int): amount of correct answers of this user.
*     - opponentScore (int): amount of correct answers of the opponent.
*     - userName (string): this user Name.
*     - opponentName (string): opponent username.
*/
function displayStatistics(userScore,opponentScore,userName,opponentName) {
    result = HTMLstatistics;
    if (userScore > opponentScore) {
        result = result.replace("%winner%",userName);
        result = result.replace("%loser%",opponentName);
        result = result.replace("%winnerScore%",userScore);
        result = result.replace("%loserScore%",opponentScore);
    }
    else {
        result = result.replace("%winner%",opponentName);
        result = result.replace("%loser%",userName);
        result = result.replace("%winnerScore%",opponentScore);
        result = result.replace("%loserScore%",userScore);
    }
    $("#container2").html("");
    $("#container2").append(result);
};

/*
* Display a question to this user.
* parameters:
*     - question (question): The question to be shown.
*/
function displayQuestion(question) {
    result = HTMLquestion;
    result = result.replace("%question%",question.pregunta);
    result = result.replace("%category%",question.categoryName);
    result = result.replace("%op1%",question.option1);
    result = result.replace("%op2%",question.option2);
    result = result.replace("%op3%",question.option3);
    result = result.replace("%op4%",question.option4);
    $("#container2").html("");
    $("#container2").append(result);
};

/*
* Display an error on the lobby.
* parameters:
*     - message (string): The error message to be shown.
*/
function displayError(message) {
    errorIsDisplayed = true;
    $("#error").html(message);
};

/*
* Erase the displayed error on the lobby.
*/
function eraseError() {
    $("#error").html("");
    errorIsDisplayed = false;
};

/*
* Display all tables on the lobby.
* parameters:
*     - tableList (array of tables): all existent tables.
*/
function displayAllTables(tableList) {
    $(".tablesContainer").html("");
    tableList.forEach(function (table) {
        $(".tablesContainer").append(createHTMLTable(table));
    });
};

/*
* Display a new table on the lobby.
* parameters:
*     - table (table): the table to be shown.
*/
function displayTable(table) {
    $(".tablesContainer").append(createHTMLTable(table));
};

/*
* Removes a table on the lobby view.
* parameters:
*     - table (table): the table to be removed.
*/
function removeTableFromView(table) {
    $("#table"+table.id).remove();
};

/*
* Generates an html table.
* parameters:
*     - table (table): the table to be formatted as html.
*/
function createHTMLTable(table) {
    var result;
    if (table.is_full == true) {
        if (table.owner_id == user.id) {
            result = HTMLUserCompleteTable;
            result = result.replace("%owner%",table.owner_username);
            result = result.replace("%guest%",table.guest_username);
            result = result.replace("%id%",table.id);
            result = result.replace("%id%",table.id);
            result = result.replace("%id%",table.id);
        }
        else {
            if (table.guest_id == user.id) {
                result = HTMLCompleteTableAsGuest;
                result = result.replace("%owner%",table.owner_username);
                result = result.replace("%guest%",table.guest_username);
                result = result.replace("%id%",table.id);
                result = result.replace("%id%",table.id);
            }
            else {
                result = HTMLCompleteTable;
                result = result.replace("%owner%",table.owner_username);
                result = result.replace("%guest%",table.guest_username);
                result = result.replace("%id%",table.id);
            }
        }
    }
    else {
        if (table.owner_id == user.id) {
            result = HTMLUserIncompleteTable;
            result = result.replace("%owner%",table.owner_username);
            result = result.replace("%id%",table.id);
        }
        else {
            result = HTMLIncompleteTable;
            result = result.replace("%owner%",table.owner_username);
            result = result.replace("%id%",table.id);
            result = result.replace("%id%",table.id);
        }
    }
    return result;
};
