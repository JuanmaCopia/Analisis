
function User(id,username) {
    this.id = id;
    this.username = username;
}

function Task(description) {
    this.description = description;
}

var user;

var HTMLCompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>%guest%</th></tr></table>';
var HTMLIncompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th><button id="entrar">Entrar</button></th></tr></table>';
var HTMLUserIncompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>Esperando</th></tr><tr><td></td><td><button id="deleteTable">Eliminar Mesa</button></td></tr></table>';
var HTMLUserCompleteTable = '<table id="table%id%"><tr><th>%owner%</th><th>VS</th><th>%guest%</th></tr><tr><td><button id="start">Comenzar Partida</button></td><td><button id="deleteTable">Eliminar Mesa</button></td><td><button id="kick">Expulsar Jugador</button></td></tr></table>';

User.prototype.getId = function() {
    return this.id;
};

User.prototype.getUsername = function() {
    return this.username;
};

function getUser() {
    $.ajax({
        type: "GET",
        url: "/user",
        async: false,
        success : function(data) {
            user = new User(data.user_id,data.username);
        }
        //$(".tablesContainer").append("<p> Holaa! "+user.getUsername()+"</p>");
    });
}

$(document).ready(getUser);

//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/lobbyy");
webSocket.onmessage = function (msg) {
    updateTables(msg);
};
webSocket.onclose = function () { alert("WebSocket connection closed") };


/*
$("crearMesa").addEventListener("click", function() {
    sendTable(user.id);
});
*/
id("createTable").addEventListener("click", function () {
    sendTable();
});

function sendTable() {
    var task = new Task("createTable");
    task.owner_id = user.getId();
    var jsonStringTask = JSON.stringify(task);
    webSocket.send(jsonStringTask);
}

function updateTables(msg) {
    var stringHtml;
    var nextColumn = 1;
    var data = JSON.parse(msg.data);
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
            result = HTMLCompleteTable;
            result = result.replace("%owner%",table.owner_username);
            result = result.replace("%guest%",table.guest_username);
            result = result.replace("%id%",table.id);
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
        }
    }
    return result;
}


function insert(targetId, message) {
    id(targetId).insertAdjacentHTML("afterbegin", message);
}


//Helper function for selecting element by id
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
