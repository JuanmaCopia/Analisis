
// Templates
var HTMLCompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>%guest%</th></tr></table>';
var HTMLIncompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th><button id="entrar" class="%id%" onclick="joinTable(%id%)">Entrar</button></th></tr></table>';
var HTMLUserIncompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>Esperando</th></tr><tr><td></td><td><button id="deleteTable" onclick="sendDeleteTable()">Eliminar Mesa</button></td></tr></table>';
var HTMLUserCompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>%guest%</th></tr><tr><td><button id="start">Comenzar Partida</button></td><td><button id="deleteTable" onclick="sendDeleteTable()">Eliminar Mesa</button></td><td><button id="kick">Expulsar Jugador</button></td></tr></table>';
var HTMLCompleteTableAsGuest = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>%guest%</th></tr><tr><td></td><td><button id="exit" onclick="exitTable()">Salir</button></td></tr></table>'
// Variables
var user;
var sittedTable = null;
var nextColumn = 1;
var errorIsDisplayed = false;

// Task constructor
function Task(description) {
    this.description = description;
}
// Table constructor
function Table(id,isOwner) {
    this.id = id;
    this.isOwner = isOwner;
}
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
}

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
}
// recovers the table information, if the user is in a table it initializes the sittedTable variable with that data.
function setTable() {
    if (id("in_tableh").innerHTML == "true") {
        if (id("is_ownerh").innerHTML == "true")
            sittedTable = new Table(id("table_idh").innerHTML,true);
        else
            sittedTable = new Table(id("table_idh").innerHTML,false);
    }
}

// function to execute when the document is ready
$(document).ready(setUser);

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
}
// This function is executed when the user wants to delete his table.
// sends the delete table task, with this user id
function sendDeleteTable() {
    var task = new Task("deleteTable");
    task.table_id = sittedTable.getId();
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
}

// This function is executed when the user wants to join to a table.
// sends the delete table task, with this user id
function joinTable(elemTable) {
    console.log(elemTable);
    /*
    var id = elemTable.className;
    console.log("el id de la mesa es:"+id);*/
    /*
    if (sittedTable == null) {
        var task = new Task("joinTable");
        task.table_id = tableId;
        var jsonStringTask = JSON.stringify(task);
        webSocket.send(jsonStringTask);
    }*/
}

// display an error message
function displayError(message) {
    errorIsDisplayed = true;
    id("error").innerHTML = message;
}

// erase the error message
function eraseError() {
    id("error").innerHTML = "";
}

// Display all tables
function displayAllTables(data) {
    console.log(data);
    id("column1").innerHTML = "";
    id("column2").innerHTML = "";
    id("column3").innerHTML = "";
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
}

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
}

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
}

// Deletes this user table
function deleteTable(table) {
    var thisUserId = user.getId();
    if (sittedTable!= null) {
        var thisUserTableId = sittedTable.getId();
        if (thisUserTableId == table.id) {
            sittedTable = null;
        }
    }
}

// remove a table from the view
function removeDeletedTable(data) {
    $("#table"+data.id).remove();
}

// creates the html table.
function createHTMLTable(table) {
    var result;
    if (table.is_full == true) {
        if (table.owner_id == user.getId()) {
            result = HTMLUserCompleteTable;
            result = result.replace("%owner%",table.owner_username);
            result = result.replace("%guest%",table.guest_username);
            result = result.replace("%id%",table.id);
        }
        else {
            if (table.guest_id == user.getId()) {
                result = HTMLCompleteTableAsGuest;
                result = result.replace("%owner%",table.owner_username);
                result = result.replace("%guest%",table.guest_username);
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
            result = result.replace("%id%",table.id);
        }
    }
    return result;
}

// inserts a message (html format string) at the begining of a html element
function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}

// Helper function for selecting element by id
function id(id) {
    return document.getElementById(id);
}
















/*
aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
    function Apple (type) {
        this.type = type;
        this.color = "red";
    }

    Apple.prototype.getInfo = function() {
        return this.color + ' ' + this.type + ' apple';
    };

    var apple = new Apple('macintosh');
    apple.color = "reddish";
    alert(apple.getInfo());

    Apple.prototype.getInfo = function() {
        return this.color + ' ' + this.type + ' apple';
    };
aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa

<table id="table">
    <tr>
        <th>Owner</th>
        <th>VS</th>
        <th>Player2</th>
    </tr>
    <tr>
        <td><button id="start">Comenzar Partida</button></td>
        <td><button id="deleteTable">Eliminar Mesa</button></td>
        <td><button id="kick">Expulsar Jugador</button></td>
    </tr>
</table>

<table id="table%id%">
    <tr>
        <th>%owner%</th>
        <th>VS</th>
        <th>%guest%</th>
    </tr>
    <tr>
        <td></td>
        <td><button id="exit">Salir</button></td>
    </tr>
</table>

<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>%guest%</th></tr><tr><td></td><td><button id="exit">Salir</button></td></tr></table>


<table id="table">
    <tr>
        <th>Owner</th>
        <th>VS</th>
        <th>Esperando</th>
    </tr>
    <tr>
        <td><button id="deleteTable">Eliminar Mesa</button></td>
    </tr>
</table>

<table id="table">
    <tr>
        <th>Owner</th>
        <th>VS</th>
        <th><button id="entrar">Entrar</button></th>
    </tr>
</table>

<table id="table">
    <tr>
        <th>Juan</th>
        <th>VS</th>
        <th>Pepe</th>
    </tr>
</table>


*/
