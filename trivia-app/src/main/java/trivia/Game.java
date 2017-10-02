
package trivia;

import java.util.*;
import org.javalite.activejdbc.Model;

public class Game extends Model {

	//Bloque estatico para poder utilizar los metodos de validacion correspondientes en las clases de Testing.
	static {
    	validatePresenceOf("user_id").message("Please, provide the user id");
    	validatePresenceOf("state").message("Please, provide the state of the game.");
  	}

  	/**
     * returns the id of the "this" game.
     * @pre. this != null
     * @return an int value that is de id of this game.
     * @post. the game id must be returned,
     */
  	public int getGId() {
		return this.getInteger("id");
	}

	/**
     * returns the amount of asked questions in this game.
     * @pre. this != null
     * @return an Integer value representing the amount of asked questions.
     * @post. the amount of asked questions must be returned.
     */
	public Integer getNumberQuestion() {
		return (Integer)this.get("cantPreg");
	}

	/**
     * returns the amount of correctly answered questions in this game.
     * @pre. this != null
     * @return an Integer value representing the amount of correctly answered questions.
     * @post. the amount of correctly answered questions must be returned.
     */
	public Integer getRightAnswers() {
		return this.getInteger("rightAnswers");
	}

	/**
     * returns the amount of incorrectly answered questions in this game.
     * @pre. this != null
     * @return an Integer value representing the amount of incorrectly answered questions.
     * @post. the amount of incorrectly answered questions must be returned.
     */
	public Integer getWrongAnswers() {
		return this.getInteger("wrongAnswers");
	}

	/**
     * returns the game's state.
     * @pre. this != null
     * @return an String value representing the game's state.
     * @post. the game's state must be returned.
     */
	public String getState() {
		return this.getString("state");
	}

	/**
     * sets the data of all the fields that corresponds to a new game.
     * @param user_id
     * @pre. this != null
     * @post. the data of the fields of a new game are initiated.
     */
	public void setBeginning(int user_id) {
		this.set("cantPreg",0);
    	this.set("user_id",user_id);
    	this.set("state","Game_In_Progress");
    	this.set("rightAnswers", 0);
    	this.set("wrongAnswers", 0);
    	this.saveIt();
	}

    /**
     * sets the game's state on "Game Over".
     * @pre. this != null
     * @post. the state of the current game should be "Game Over".
     */
	public void setStateGameOver() {
		this.set("state","Game_Over");
		this.saveIt();
	}

	/**
     * This method increases the question's counter of the game.
     * @pre. this != null
     * @post. the question's counter of the current game should be increased by 1.
     */
	public void increaseNumberQuestion() {
		this.set("cantPreg",(Integer)this.get("cantPreg")+1);
        this.saveIt();
	}

    /**
     * This method checks if the option chosen by the user is right or not, and assigns the correspondent score to Game and User.
     * @param question_id, user_id, game_id, option
     * @pre. this != null
     * @post. the correspondent score of Game and User should be increased by 1.
     */
    public static void answerQuestion(int question_id, int user_id,int game_id, int option) {
		Question q = Question.findById(question_id);
		int correctAnswer = q.getCorrectOption();
		Game g = Game.findById(game_id);
		User u = User.findById(user_id);
		if (option == correctAnswer) {
			u.increaseScore();
			g.set("rightAnswers",g.getInteger("rightAnswers") + 1);
			g.saveIt();
		}
		else {
			u.decreaseScore();
			g.set("wrongAnswers",g.getInteger("wrongAnswers") + 1);
			g.saveIt();
		}

	}

}
