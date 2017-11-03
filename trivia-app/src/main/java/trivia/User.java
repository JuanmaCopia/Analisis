
package trivia;

import java.util.*;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.validation.UniquenessValidator;

import org.json.JSONObject;

public class User extends Model {

    //Bloque estatico para poder utilizar los metodos de validacion correspondientes en las clases de Testing.
    static{
        validatePresenceOf("username").message("Please, provide your username");
        validatePresenceOf("password").message("Please, provide your password");
        validatePresenceOf("email").message("Please, provide your email");
        validateEmailOf("email");
        validateWith(new UniquenessValidator("username")).message("This username is already taken.");
        validateWith(new UniquenessValidator("email")).message("This email is already taken.");
    }


    public JSONObject toJson() {
        JSONObject result = new JSONObject();
        result.put("user_id",this.getInteger("id"));
        result.put("username",this.getString("username"));
        return result;
    }

    /**
     * returns the id of the "this" user.
     * @pre. this != null
     * @return an int value that is de id of this user.
     * @post. the user id must be returned,
     */
    public Integer getUId() {
        return this.getInteger("id");
    }

    /**
     * returns the total amount of correctly answered questions of this user.
     * @pre. this != null
     * @return an int value that is the total amount of correctly answered questions.
     * @post. the total amount of correctly answered questions of this user must be returned.
     */
    public int rightAnswers() {
        return this.getInteger("rightAnswers");
    }

    /**
     * returns username of this user.
     * @pre. this != null
     * @return an String value that is the username of this user.
     * @post. this user's username must be returned.
     */
    public String getUsername() {
        return (String) this.get("username");
    }

    /**
     * sets the data of this user.
     * @param username
     * @param password
     * @param email
     * @pre. this != null, this user must be a new user, username and email should be properly formed.
     * @post. the input data should be equal to its corresponding database fields.
     */
    public void setSignUpData(String username, String password, String email) {
        this.set("username",username);
        this.set("password",password);
        this.set("email",email);
        this.set("rightAnswers",0);
        this.set("wrongAnswers",0);
        this.saveIt();
    }

    /**
     * increase the score of this user by 1.
     * @pre. this != null
     * @post. the amount of rigthAnswers must be increased by 1.
     */
    public void increaseScore() {
        this.set("rightAnswers",this.getInteger("rightAnswers")+1);
        this.saveIt();
    }

    /**
     * decrease the score of this user.
     * @pre. this != null
     * @post. the amount of wrongAnswers must be increased by 1.
     */
    public void decreaseScore() {
        this.set("wrongAnswers",this.getInteger("wrongAnswers")+1);
        this.saveIt();
    }
}
