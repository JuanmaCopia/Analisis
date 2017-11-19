
package trivia;

public class UserInfo {

    private int id;
    private String username;

    public UserInfo() {
        this.id = -1;
        this.username = null;
    }

    public UserInfo(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
