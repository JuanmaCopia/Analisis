
package trivia;
//This class will carry the user data (id and username) to use it in the userMap of the App class.
public class UserInfo {

    private int id;
    private String username;

    /**
     * Class constructor.
     * @pre. true
     * @post this.id = -1 and this.username = null.
    */
    public UserInfo() {
        this.id = -1;
        this.username = null;
    }

    /**
     * Class constructor.
     * @pre. true
     * @param id,username
     * @post this.id = id and this.username = username.
    */
    public UserInfo(int id, String username) {
        this.id = id;
        this.username = username;
    }

    /**
     * returns the id.
     * @pre. this != null
     * @return an int value that is the id of the user.
     * @post. the user id must be returned.
     */
    public int getId() {
        return this.id;
    }

    /**
     * sets the id.
     * @pre. this != null
     * @param id
     * @post this.id = id.
    */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * returns the username.
     * @pre. this != null
     * @return a String that is the username.
     * @post. the username must be returned.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * sets the username.
     * @pre. this != null
     * @param username
     * @post this.username = username.
    */
    public void setUsername(String username) {
        this.username = username;
    }

}
