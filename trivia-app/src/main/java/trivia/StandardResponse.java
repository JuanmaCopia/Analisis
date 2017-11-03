
package trivia;

import org.json.JSONObject;

public class StandardResponse {

    private String status;
    private String message;
    private JSONObject data;

    public StandardResponse(String status) {
        this.status = status;
    }
    public StandardResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }
    public StandardResponse(String status, JSONObject data) {
        this.status = status;
        this.data = data;
    }

    // getters and setters
}
