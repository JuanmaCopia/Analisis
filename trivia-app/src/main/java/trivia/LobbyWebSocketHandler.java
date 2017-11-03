/**
 * lobby webSocket controller.
*/
package trivia;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.json.JSONObject;
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
        String username = "User" + App.nextUserNumber++;
        App.userUsernameMap.put(user, username);
        App.refreshTables();
    }

    /**
     * Actions to perform when a user disconnects from the server
    */
    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
        String username = App.userUsernameMap.get(user);
        App.userUsernameMap.remove(user);
    }
    /**
     * Actions to perform when a message from the client arrives.
    */
    @OnWebSocketMessage
    public void onMessage(Session user,String message) {
        JSONObject task = new JSONObject(message);
        int userId,tableId,ownerId,guestId;
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
                // deberia chequear que el usuario es efectivamente el dueño de la mesa
                tableId = task.getInt("table_id");
                App.sendDeletedTable(tableId);
                break;
            case "asda":
                break;
            case "asdasd":
                break;
            default:
        }
    }

}
