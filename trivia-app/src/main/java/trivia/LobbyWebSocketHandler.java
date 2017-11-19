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
        App.refreshTables();
    }

    /**
     * Actions to perform when a user disconnects from the server
    */
    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        UserInfo userInfo = App.userMap.get(user);
        int userId = userInfo.getId();
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        List<Table> tablesList = Table.findBySQL("SELECT * FROM tables WHERE owner_id = "+userId+" or guest_id = "+userId);
        if (!tablesList.isEmpty()) {
            Table table = tablesList.get(0);
            if (table.getOwnerId() == userId) {
                Base.close();
                App.sendDeletedTable(table.getTableId());
            }
            else {
                Base.close();
                table.deleteGuestUser();
            }
        }
        else {
            Base.close();
        }
        App.userMap.remove(user);
    }
    /**
     * Actions to perform when a message from the client arrives.
    */
    @OnWebSocketMessage
    public void onMessage(Session user,String message) {
        JSONObject task = new JSONObject(message);
        int userId,tableId,ownerId,guestId,matchId,questionId,userAnswer;
        Table table;
        String description = new String(task.getString("description"));
        switch (description) {
            case "createTable":
                userId = task.getInt("owner_id");
                Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
                List<Table> tablesList = Table.findBySQL("SELECT * FROM tables WHERE owner_id = "+userId+" or guest_id = "+userId);
                if (tablesList.isEmpty()) {
                    Table newTable = new Table();
                    newTable.initialize(userId);
                    Base.close();
                    App.sendCreatedTable(newTable);
                }
                else {
                    Base.close();
                    App.tableCreationError(user);
                }
                break;
            case "deleteTable":
                tableId = task.getInt("table_id");
                App.sendDeletedTable(tableId);
                break;
            case "joinTable":
                guestId = task.getInt("guest_id");
                tableId = task.getInt("table_id");
                Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
                table = Table.findById(tableId);
                table.setGuestUser(guestId);
                Base.close();
                App.userJoinedTable(table);
                App.refreshTables();
                break;
            case "guestLeft":
                tableId = task.getInt("table_id");
                Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
                Table table2 = Table.findById(tableId);
                guestId = table2.getGuestId();
                Base.close();
                table2.deleteGuestUser();
                App.guestLeftTable(table2,guestId);
                App.refreshTables();
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
                Base.close();
                App.sendMatchQuestions(questionsArray,ownerId,guestId,matchId);
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
                Base.close();
                App.answerQuest(user,userAnswer,correctAnswer,matchId,userId);
                break;
            case "setUser":
                App.userMap.put(user, new UserInfo(task.getInt("user_id"),task.getString("username")));
                break;
            case "ping":
                break;
        }
    }

}
