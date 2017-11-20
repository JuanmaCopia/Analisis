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

    private String sender, msg;

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
                Base.close();
                String taskIdentifier = new String("tableDeleted");
                App.sendTable(deletedTable,taskIdentifier);
            }
            else {
                table.deleteGuestUser();
                App.refreshTables(Table.getTables());
                Base.close();
            }
        }
        else {
            Base.close();
        }
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
        switch (description) {
            case "createTable":
                userId = task.getInt("owner_id");
                Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
                List<Table> tablesList = Table.getUserTable(userId);
                if (tablesList.isEmpty()) {
                    Table newTable = new Table();
                    newTable.initialize(userId);
                    jsonTable = newTable.toJson();
                    taskIdentifier = new String("displayCreatedTable");
                    App.sendTable(jsonTable,taskIdentifier);
                }
                else {
                    String errorMsg = new String("You are inside a table already.");
                    App.sendError(user,errorMsg);
                }
                Base.close();
                break;
            case "deleteTable":
                tableId = task.getInt("table_id");
                Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
                table = Table.findById(tableId);
                JSONObject deletedTable = table.toJson();
                table.deleteCascadeShallow();
                Base.close();
                taskIdentifier = new String("tableDeleted");
                App.sendTable(deletedTable,taskIdentifier);
                break;
            case "joinTable":
                guestId = task.getInt("guest_id");
                tableId = task.getInt("table_id");
                Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
                table = Table.findById(tableId);
                table.setGuestUser(guestId);
                jsonTable = table.toJson();
                taskIdentifier = new String("userJoined");
                App.sendTable(jsonTable,taskIdentifier);
                tableArray = Table.getTables();
                Base.close();
                App.refreshTables(tableArray);
                break;
            case "guestLeft":
                tableId = task.getInt("table_id");
                Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
                table = Table.findById(tableId);
                guestId = table.getGuestId();
                jsonTable = table.toJson();
                table.deleteGuestUser();
                taskIdentifier = new String("userLeftTable");
                App.sendTable(jsonTable,taskIdentifier);
                tableArray = Table.getTables();
                Base.close();
                App.refreshTables(tableArray);
                break;
            case "startMatch":
                tableId = task.getInt("table_id");
                Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
                table = Table.findById(tableId);
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
                Base.close();
                App.sendMatchQuestions(questionsArray,ownerId,guestId,matchId,guestName,ownerName);
                break;
            case "checkAnswer":
                matchId = task.getInt("match_id");
                userId = task.getInt("user_id");
                questionId = task.getInt("question_id");
                userAnswer = task.getInt("userAnswer");
                Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
                Match match = Match.findById(matchId);
                Question q = Question.findById(questionId);
                int correctAnswer = q.getCorrectOption();
                if (!match.isOver() && (userAnswer == correctAnswer)) {
                    match.incrementScore(userId);
                }
                int userScore = match.getUserScore(userId);
                int opponentScore = match.getOpponentScore(userId);
                boolean matchOver = match.isOver();
                Base.close();
                App.answerQuest(user,userAnswer,correctAnswer,matchOver,userScore,opponentScore);
                break;
            case "setUser":
                App.userMap.put(user, new UserInfo(task.getInt("user_id"),task.getString("username")));
                break;
            case "ping":
                break;
        }
    }

}
