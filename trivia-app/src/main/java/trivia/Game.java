
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

	//Setea todos los campos que corresponden al inicio de un juego.
	public void setBeginning(int user_id) {
		this.set("cantPreg",0);
    	this.set("user_id",user_id);
    	this.set("state","Game_In_Progress");
    	this.set("rightAnswers", 0);
    	this.set("wrongAnswers", 0);
    	this.saveIt();
	}

	//Setea el estado de un juego en Game Over (para juego finalizado).
	public void setStateGameOver() {
		this.set("state","Game_Over");
		this.saveIt();
	}

	//Incrementa en uno el contador de preguntas del juego.
	public void increaseNumberQuestion() {
		this.set("cantPreg",(Integer)this.get("cantPreg")+1);
        this.saveIt();
	}

	//Se encarga de chequear si la opcion elegida por el usuario como respuesta a una pregunta es correcta o
	//incorrecta, y asigna el puntaje correspondiente para Game y User.
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
