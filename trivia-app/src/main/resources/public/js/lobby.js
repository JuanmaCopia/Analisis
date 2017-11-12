
// Tables Templates
var HTMLCompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>%guest%</th></tr></table>';
var HTMLIncompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th><button id="entrar" onclick="sendJoinTable(%id%)">Entrar</button></th></tr></table>';
var HTMLUserIncompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>Esperando</th></tr><tr><td></td><td><button id="deleteTable" onclick="sendDeleteTable()">Eliminar Mesa</button></td></tr></table>';
var HTMLUserCompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>%guest%</th></tr><tr><td><button id="start" onclick="sendStartGame(%id%)">Comenzar Partida</button></td><td><button id="deleteTable" onclick="sendDeleteTable()">Eliminar Mesa</button></td><td><button id="kick" onclick="sendGuestLeft(%id%)">Expulsar Jugador</button></td></tr></table>';
var HTMLCompleteTableAsGuest = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>%guest%</th></tr><tr><td></td><td><button id="exit" onclick="sendGuestLeft(%id%)">Salir</button></td></tr></table>';
// Question Template
var HTMLquestion = '<div class="questionBox"><div class="question"><p>Categoria: <span id="category"></span></p><p id="questionText"></p></div><div class="options"><button id="option1" onclick="answerQuestion(1)"></button><button id="option2" onclick="answerQuestion(2)"></button><button id="option3" onclick="answerQuestion(3)"></button><button id="option4" onclick="answerQuestion(4)"></button></div></div>';
// Variables
var user;
var sittedTable = null;
var nextColumn = 1;
var errorIsDisplayed = false;
var matchQuestionsArray;
var currentQuestion;

// Task constructor
function Task(description) {
    this.description = description;
};
// Table constructor
function Table(id,isOwner) {
    this.id = id;
    this.isOwner = isOwner;
};
// Table function: returns the table id.
Table.prototype.getId = function() {
    return this.id;
};

// Table function: returns if this user is the owner of the table or not.
Table.prototype.isOwner = function() {
    return this.isOwner;
};

// User constructor
function User(id,username) {
    this.id = id;
    this.username = username;
};

// User function: returns the user id.
User.prototype.getId = function() {
    return this.id;
};

// User function: returns the user username.
User.prototype.getUsername = function() {
    return this.username;
};

// recovers the user information and initialize the user variable with that data.
function setUser() {
    user = new User(id("idh").innerHTML,id("nameh").innerHTML);
};

// recovers the table information, if the user is in a table it initializes the sittedTable variable with that data.
function setTable() {
    var elemT = id("in_tableh");
    if (elemT.innerHTML == "true") {
        elemT.innerHTML = "false";
        if (id("is_ownerh").innerHTML == "true")
            sittedTable = new Table(id("table_idh").innerHTML,true);
        else
            sittedTable = new Table(id("table_idh").innerHTML,false);
    }
};

//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/lobbyy");

// Actions to execute when a message is received.
webSocket.onmessage = function (msg) {
    var data = JSON.parse(msg.data);
    switch(data.task) {
        case "refreshTables":
            if (user == null)
                setUser();
            if (sittedTable == null)
                setTable();
            displayAllTables(data);

        break;
        case "displayCreatedTable":
            createTable(data.newTable);
            displayCreatedTable(data.newTable);
        break;
        case "creationError":
            displayError('Ya estas dentro de una mesa');
        break;
        case "tableDeleted":
            deleteTable(data.deletedTable);
            removeDeletedTable(data.deletedTable);
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
        case "gameQuestions":
            //matchQuestionsArray = data.questionsList;
        break;
        default:
            displayError('Error desconocido');
    }
};

// Actions to execute when a the websocket connection is closed.
webSocket.onclose = function () { alert("WebSocket connection closed") };

// Adds an onclick event to the create Table button
id("createTable").addEventListener("click", function () {
    if (sittedTable == null) {
        if (errorIsDisplayed) {
            eraseError();
        }
        sendTable();
    }
    else {
        displayError('Ya estas dentro de una mesa');
    }
});

// This function is executed when the user wants to create a table.
// sends the create table task, with this user id
function sendTable() {
    var task = new Task("createTable");
    task.owner_id = user.getId();
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
};
// This function is executed when the user wants to delete his table.
// sends the delete table task, with this user id
function sendDeleteTable() {
    var task = new Task("deleteTable");
    task.table_id = sittedTable.getId();
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
};

// This function is executed when the user wants to join to a table.
// sends the join table task, with the table id
function sendJoinTable(tableId) {
    if (sittedTable == null) {
        var task = new Task("joinTable");
        task.table_id = tableId;
        task.guest_id = user.id;
        var jsonStringTask = JSON.stringify(task);
        webSocket.send(jsonStringTask);
    }
    else {
        displayError("Debes salir de la mesa actual antes de unirte a otra");
    }
};

// This function is executed when the user wants to leave a table.
// sends the exit table task, with the table id
function sendGuestLeft(tableId) {
    var task = new Task("guestLeft");
    task.table_id = tableId;
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
};

// This function is executed when the owner start the game with another player.
// sends the start game task, with the table id
function sendStartGame(tableId) {
    var task = new Task("startGame");
    task.table_id = tableId;
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
};

// display an error message
function displayError(message) {
    errorIsDisplayed = true;
    id("error").innerHTML = message;
};

// erase the error message
function eraseError() {
    id("error").innerHTML = "";
};

// Display all tables
function displayAllTables(data) {
    id("column1").innerHTML = "";
    id("column2").innerHTML = "";
    id("column3").innerHTML = "";
    nextColumn = 1;
    data.tableList.forEach(function (table) {
        insert("column"+nextColumn.toString(),createHTMLTable(table));
        switch(nextColumn) {
            case 1:
                nextColumn = 2;
            break;
            case 2:
                nextColumn = 3;
            break;
            case 3:
                nextColumn = 1;
            break;
        }
    });
};

// Display the created table
function displayCreatedTable(data) {
    insert("column"+nextColumn.toString(),createHTMLTable(data));
    switch(nextColumn) {
        case 1:
            nextColumn = 2;
        break;
        case 2:
            nextColumn = 3;
        break;
        case 3:
            nextColumn = 1;
        break;
    }
};

// Creates the new table
function createTable(table) {
    var thisUserId = user.getId();
    if (thisUserId == table.owner_id) {
        sittedTable = new Table(table.id,true);
    }
    else {
        if (thisUserId == table.guest_id) {
            sittedTable = new Table(table.id,false);
        }
    }
};

// Deletes this user table
function deleteTable(table) {
    var thisUserId = user.getId();
    if (sittedTable!= null) {
        var thisUserTableId = sittedTable.getId();
        if (thisUserTableId == table.id) {
            sittedTable = null;
        }
    }
};

// remove a table from the view
function removeDeletedTable(data) {
    $("#table"+data.id).remove();
};

// creates the html table.
function createHTMLTable(table) {
    var result;
    if (table.is_full == true) {
        if (table.owner_id == user.getId()) {
            result = HTMLUserCompleteTable;
            result = result.replace("%owner%",table.owner_username);
            result = result.replace("%guest%",table.guest_username);
            result = result.replace("%id%",table.id);
            result = result.replace("%id%",table.id);
            result = result.replace("%id%",table.id);
        }
        else {
            if (table.guest_id == user.getId()) {
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
        if (table.owner_id == user.getId()) {
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

// inserts a message (html format string) at the begining of a html element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
};

// Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
};

