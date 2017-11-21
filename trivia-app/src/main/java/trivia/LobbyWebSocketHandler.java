/**
 * lobby webSocket controller.
*/
package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.json.JSONObject;
import org.json.JSONArray;
import org.javalite.activejdbc.Base;
import java.util.*;

@WebSocket
public class LobbyWebSocketHandler {

    /**
     * Actions to perform when a user connects to the server
    */
    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        App.userMap.put(user, new UserInfo());
        App.nextUserNumber++;
        App.updateOnlineUsers();
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        JSONArray tableArray = Table.getTables();
        Base.close();
        App.refreshTablesForUser(user,tableArray);
    }

    /**
     * Actions to perform when a user disconnects from the server
    */
    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        UserInfo userInfo = App.userMap.get(user);
        int userId = userInfo.getId();
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        List<Table> tablesList = Table.getUserTable(userId);
        if (!tablesList.isEmpty()) {
            Table table = tablesList.get(0);
            if (table.getOwnerId() == userId) {
                JSONObject deletedTable = table.toJson();
                table.deleteCascadeShallow();
                String taskIdentifier = new String("tableDeleted");
                App.sendTable(deletedTable,taskIdentifier);
            }
            else {
                table.deleteGuestUser();
                App.refreshTables(Table.getTables());
            }
        }
        Base.close();
        App.userMap.remove(user);
        App.nextUserNumber--;
        App.updateOnlineUsers();
    }

    /**
     * Actions to perform when a message from the client arrives.
    */
    @OnWebSocketMessage
    public void onMessage(Session user,String message) {
        JSONObject task = new JSONObject(message);
        int userId,tableId,ownerId,guestId,matchId,questionId,userAnswer;
        Table table;
        String taskIdentifier;
        JSONObject jsonTable;
        JSONArray tableArray;
        String description = new String(task.getString("description"));
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        switch (description) {
            case "createTable":
                userId = task.getInt("owner_id");
                List<Table> tablesList = Table.getUserTable(userId);
                if (tablesList.isEmpty()) {
                    Table newTable = new Table();
                    newTable.initialize(userId);
                    jsonTable = newTable.toJson();
                    taskIdentifier = new String("displayCreatedTable");
                    App.sendTable(jsonTable,taskIdentifier);
                }
                else {
                    App.sendError(user,"Ya estas dentro de una mesa.");
                }
                break;
            case "deleteTable":
                tableId = task.getInt("table_id");
                table = Table.findById(tableId);
                JSONObject deletedTable = table.toJson();
                table.deleteCascadeShallow();
                taskIdentifier = new String("tableDeleted");
                App.sendTable(deletedTable,taskIdentifier);
                break;
            case "joinTable":
                guestId = task.getInt("guest_id");
                tableId = task.getInt("table_id");
                table = Table.findById(tableId);
                if (table != null) {
                    if (!table.isFull()) {
                        table.setGuestUser(guestId);
                        jsonTable = table.toJson();
                        taskIdentifier = new String("userJoined");
                        App.sendTable(jsonTable,taskIdentifier);
                        tableArray = Table.getTables();
                        App.refreshTables(tableArray);
                    }
                    else {
                        App.sendError(user,"Esa mesa esta llena.");
                    }
                }
                else {
                    App.sendError(user,"Esa mesa ya no esta disponible.");
                }
                break;
            case "guestLeft":
                tableId = task.getInt("table_id");
                table = Table.findById(tableId);
                if (table != null) {
                    jsonTable = table.toJson();
                    table.deleteGuestUser();
                    taskIdentifier = new String("userLeftTable");
                    App.sendTable(jsonTable,taskIdentifier);
                    tableArray = Table.getTables();
                    App.refreshTables(tableArray);
                }
                else {
                    App.sendError(user,"La mesa ya fue eliminada");
                }
                break;
            case "startMatch":
                tableId = task.getInt("table_id");
                table = Table.findById(tableId);
                if (table.isFull()) {
                    guestId = table.getGuestId();
                    ownerId = table.getOwnerId();
                    Match m = new Match();
                    m.setMatchBeginning(ownerId,guestId);
                    matchId = m.getMId();
                    JSONArray questionsArray = Question.getMatchQuestions();
                    User owner = User.findById(ownerId);
                    User guest = User.findById(guestId);
                    String ownerName = owner.getUsername();
                    String guestName = guest.getUsername();
                    App.sendMatchQuestions(questionsArray,ownerId,guestId,matchId,guestName,ownerName);
                }
                else {
                    App.sendError(user,"El otro jugador salio de la mesa");
                }
                break;
            case "checkAnswer":
                matchId = task.getInt("match_id");
                userId = task.getInt("user_id");
                questionId = task.getInt("question_id");
                userAnswer = task.getInt("userAnswer");
                Match match = Match.findById(matchId);
                Question q = Question.findById(questionId);
                int correctAnswer = q.getCorrectOption();
                if (!match.isOver() && (userAnswer == correctAnswer)) {
                    match.incrementScore(userId);
                }
                int userScore = match.getUserScore(userId);
                int opponentScore = match.getOpponentScore(userId);
                boolean matchOver = match.isOver();
                App.answerQuest(user,userAnswer,correctAnswer,matchOver,userScore,opponentScore);
                break;
            case "setUser":
                App.userMap.put(user, new UserInfo(task.getInt("user_id"),task.getString("username")));
                break;
            case "ping":
                break;
        }
        Base.close();
    }

}
