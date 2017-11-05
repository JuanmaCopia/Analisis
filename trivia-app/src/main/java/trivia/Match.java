package trivia;

import java.util.*;
import org.javalite.activejdbc.Model;

public class Match extends Model {

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
}
