
// Variables
var user;
var sittedTable;
var match;
var errorIsDisplayed = false;

/*
* Match Constructor: initializes this user Match.
* parameters:
*     - id (int): the match id.
*     - questionsArray (array of questions): all the questions of the match.
*     - thisUserName (string): this user Name.
*     - opponentName (string): opponent username.
*     - lastQuestionId (int): the id of the last asked question.
*/
function Match(id,questionsArray,thisUserName,opponentName,lastQuestionId) {
    this.id = id;
    this.questionsArray = questionsArray;
    this.thisUserName = thisUserName;
    this.opponentName = opponentName;
    this.lastQuestionId = lastQuestionId;
    this.currentQuestion = 0;
};

/*
* Task Constructor: initializes a Task to be send.
* parameters:
*     - description (string): the task identifier.
*/
function Task(description) {
    this.description = description;
};

/*
* Table Constructor: initializes this user table.
* parameters:
*     - id (int): the table id.
*     - isOwner (boolean): Indicates if this user isOwner of this table or not.
*/
function Table(id,isOwner) {
    this.id = id;
    this.isOwner = isOwner;
};

/*
* User Constructor: initializes this user.
* parameters:
*     - id (int): this user id.
*     - username (string): this user username.
*/
function User(id,username) {
    this.id = id;
    this.username = username;
};

/*
* recovers the user information and initialize the user variable with that data.
*/
function setUser() {
    user = new User($("#idh").html(),$("#nameh").html());
};

/*
* recovers the table information, if the user is sitted in a table it initializes the sittedTable variable with that data.
*/
function setTable() {
    var elemT = $("#in_tableh");
    if (elemT.html() == "true") {
        elemT.html("false");
        if ($("#is_ownerh").html() == "true")
            sittedTable = new Table($("#table_idh").html(),true);
        else
            sittedTable = new Table($("#table_idh").html(),false);
    }
};

/*
* Adds a click event listener to the "createTable" button, when its clicked, it sends a request to the server
* to create a new table. If this user is already sitted in a table shows an error message.
*/
$("#createTable").click(function(){
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

/*
* Sends the user answer to a question.
* parameters:
*     - id (optionNumber): the number of the answered option.
*/
function sendAnswer(optionNumber) {
    removeOnclickEventsOptions();
    var task = new Task("checkAnswer");
    task.match_id = match.id;
    task.user_id = user.id;
    task.question_id = match.lastQuestionId;
    task.userAnswer = optionNumber;
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
};

/*
* Sends the "createTable" task, that is a request to the server to create a new table with this user as owner.
*/
function sendTable() {
    var task = new Task("createTable");
    task.owner_id = user.id;
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
};

/*
* Sends the "deleteTable" task, that is a request to the server to delete a table that this user is the owner.
*/
function sendDeleteTable() {
    var task = new Task("deleteTable");
    task.table_id = sittedTable.id;
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
};

/*
* Sends the "joinTable" task, that is a request to the server of this user to join in another table.
* If the user in already in a table, displays an error message.
* parameters:
*     - tableId (int): the table id.
*/
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

/*
* Sends the "sendGuestLeft" task, that is a request to the server to remove a guest from a specific table.
* parameters:
*     - tableId (int): the table id of the table to remove the guest.
*/
function sendGuestLeft(tableId) {
    var task = new Task("guestLeft");
    task.table_id = tableId;
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
};

/*
* Sends the "startMatch" task, that is a request to the server to begin a match between 2 players.
* parameters:
*     - tableId (int): the table id where the players are sitted.
*/
function sendStartGame(tableId) {
    var task = new Task("startMatch");
    task.table_id = tableId;
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
};

/*
* Initialize the sittedTable variable only if this user is the owner.
* parameters:
*     - table (table): the created table.
*/
function createTable(table) {
    if (user.id == table.owner_id) {
        sittedTable = new Table(table.id,true);
    }
};

/*
* Sets the sittedTable variable to null only if this user was sitted on the deleted table.
* parameters:
*     - table (table): the deleted table.
*/
function deleteTable(table) {
    if (sittedTable!= null) {
        var thisUserTableId = sittedTable.id;
        if (thisUserTableId == table.id) {
            sittedTable = null;
        }
    }
};

/*
* Sends the "setUser" task, that is a request to the server to set this user data.
*/
function sendUserInfo() {
    var task = new Task("setUser");
    task.user_id = user.id;
    task.username = user.username;
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
};

/*
* makes a ping to the server.
*/
function sendPing() {
    var task = new Task("ping");
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
};

/*
* makes a ping to the server every 50 seconds.
*/
setInterval(function() {
    sendPing();
},50000);






