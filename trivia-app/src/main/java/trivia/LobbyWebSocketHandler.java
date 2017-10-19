package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.json.JSONObject;
import org.javalite.activejdbc.Base;

@WebSocket
public class LobbyWebSocketHandler {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        String username = "User" + App.nextUserNumber++;
        App.userUsernameMap.put(user, username);
        App.refreshTables();
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = App.userUsernameMap.get(user);
        App.userUsernameMap.remove(user);
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        Base.open("com.mysql.jdbc.Driver", "jdbc:mysql://localhost/trivia", "root", "root");
        JSONObject task = new JSONObject(message);
        String description = new String(task.getString("description"));
        switch (description) {
            case "createTable":
                Table newTable = new Table();
                newTable.initialize(task.getInt("owner_id"));
                Base.close();
                App.refreshTables();
                break;
            case "asd":
                break;
            case "asda":
                break;
            case "asdasd":
                break;
            default:
        }
    }

}
