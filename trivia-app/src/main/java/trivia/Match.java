package trivia;

import java.util.*;
import org.javalite.activejdbc.Model;

public class Match extends Model {

    //Bloque estatico para poder utilizar los metodos de validacion correspondientes en las clases de Testing.
    static {
        validatePresenceOf("user1Id").message("Please, provide the user1Id");
        validatePresenceOf("user2Id").message("Please, provide the user2Id");
        validatePresenceOf("user1Score").message("Please, provide the user1Score");
        validatePresenceOf("user2Score").message("Please, provide the user2Score");
        validatePresenceOf("state").message("Please, provide the state");
        validateRange("user1Score", 0, 15).message("user1Score.outside.limits");
        validateRange("user2Score", 0, 15).message("user2Score.outside.limits");
    }

    /**
     * returns the id of the current object.
     * @pre. this != null
     * @return an int value that is the id of this match.
     * @post. the match id must be returned.
     */
    public int getMId() {
        return this.getInteger("id");
    }

    /**
     * returns the user1's id of the current object.
     * @pre. this != null
     * @return an int value that is the user1Id of this match.
     * @post. the user1Id of the match must be returned.
     */
    public int getUser1Id() {
        return this.getInteger("user1Id");
    }

    /**
     * returns the user2's id of the current object.
     * @pre. this != null
     * @return an int value that is the user2Id of this match.
     * @post. the user2Id of the match must be returned.
     */
    public int getUser2Id() {
        return this.getInteger("user2Id");
    }

    /**
     * returns the user1's score of the current object.
     * @pre. this != null
     * @return an int value that is the user1Score of this match.
     * @post. the user1Score of the match must be returned.
     */
    public int getUser1Score() {
        return this.getInteger("user1Score");
    }

    /**
     * returns the user2's score of the current object.
     * @pre. this != null
     * @return an int value that is the user2Score of this match.
     * @post. the user2Score of the match must be returned.
     */
    public int getUser2Score() {
        return this.getInteger("user2Score");
    }

    /**
     * returns the user's score of the current object.
     * @param userId
     * @return an int value that is the user's score of this match.
     * @post. the user's score of the match must be returned.
     */
    public int getUserScore(int userId) {
        if (userId == this.getInteger("user1Id")) {
            return this.getInteger("user1Score");
        }
        return this.getInteger("user2Score");
    }

    /**
     * returns the user's opponent score of the current object.
     * @param userId
     * @return an int value that is the user's opponent score of this match.
     * @post. the user's opponent score of the match must be returned.
     */
    public int getOpponentScore(int userId) {
        if (userId == this.getInteger("user1Id")) {
            return this.getInteger("user2Score");
        }
        return this.getInteger("user1Score");
    }

    /**
     * returns the winner's id of the current object.
     * @pre. this != null
     * @return an int value that is the winnerId of this match.
     * @post. the winnerId of the match must be returned.
     */
    public int getWinnerId() {
        return this.getInteger("winnerId");
    }

    /**
     * returns the state of the match.
     * @pre. this != null
     * @return an String value representing the state of the match.
     * @post. the state of the match must be returned.
     */
    public String getMatchState() {
        return this.getString("state");
    }

    /**
     * sets the winner's id of the current object.
     * @param userId
     * @pre. this != null
     * @post. the winnerId of the match must be the userId.
     */
    public void setWinnerId(int userId) {
        this.set("winnerId",userId);
        this.saveIt();
    }

    /**
     * sets the data of all the fields that corresponds to a new match.
     * @param user_id
     * @pre. this != null
     * @post. the data of the fields of a new match are initiated.
     */
    public void setMatchBeginning(int owner_id, int guest_id) {
        this.set("user1Id",owner_id);
        this.set("user2Id",guest_id);
        this.set("state","Game_In_Progress");
        this.set("user1Score", 0);
        this.set("user2Score", 0);
        this.set("winnerId",null);
        this.saveIt();
    }

    /**
     * sets the state of the match on "Game Over".
     * @pre. this != null
     * @post. the state of the current match should be "Game Over".
     */
    public void setMatchStateGameOver() {
        this.set("state","Game_Over");
        this.saveIt();
    }

    /**
     * returns true if the state of the match is "Game_Over".
     * @pre. this != null
     * @return an boolean value that tells if the state of the match is "Game_Over" or not.
     * @post. a boolean value must be returned.
     */
    public boolean isOver() {
        return (this.getInteger("winnerId")!=null);
    }

    /**
     * This method increments the score of the user and sets the corresponding data into the winnerId and state fields of the match when needed.
     * @param userId
     * @pre. this != null
     * @post. the correspondent score should be increased, and the data of the winnerId and state fields of the match should be modified when needed.
     */
    public void incrementScore(int userId) {
        if (userId == this.getInteger("user1Id")) {
            this.set("user1Score",this.getInteger("user1Score") + 1);
            this.saveIt();
            if (this.getInteger("user1Score") >= 10) {
                this.set("winnerId",this.getInteger("user1Id"));
                this.set("state","Game_Over");
                this.saveIt();
            }
        }
        else {
            this.set("user2Score",this.getInteger("user2Score") + 1);
            this.saveIt();
            if (this.getInteger("user2Score") >= 10) {
                this.set("winnerId",this.getInteger("user2Id"));
                this.set("state","Game_Over");
                this.saveIt();
            }
        }
    }
}
